// Snatchfreezer controller for Arduino Nano (ATmega328).
//
// The frontend uploads a program (one action per enabled table), then starts
// it. The main loop never blocks: serial input is parsed byte by byte and all
// timing is done by comparing elapsed time, so it is safe against the
// micros() / millis() overflow.
//
// No watchdog on purpose: the old Nano bootloader (cpu=atmega328old) does not
// switch it off after a watchdog reset and would hang in a reset loop.

#include "Arduino.h"
#include "Protocol.h"

// ---- program ---------------------------------------------------------------

struct Timing
{
  uint32_t delay;
  uint32_t release;
  uint32_t delayIncrement;
  uint32_t releaseIncrement;
};

enum ActionState : uint8_t
{
  WAITING, // output low, waiting for the delay to pass
  ACTIVE,  // output high, waiting for the release time to pass
  DONE
};

struct Action
{
  uint8_t pin;
  uint8_t count;
  Timing timings[MAX_TIMINGS];
  // while running
  uint8_t index;
  ActionState state;
  uint32_t mark; // start of the current step
  uint32_t wait; // length of the current step
};

Action actions[MAX_ACTIONS];
uint8_t actionCount = 0;
uint16_t cycles = 1;
uint32_t cycleDelayMs = 0;

enum RunState : uint8_t
{
  IDLE,
  CYCLE,      // actions are being executed
  CYCLE_PAUSE // waiting for the cycle delay
};

RunState runState = IDLE;
uint16_t cycle = 0;
uint8_t remaining = 0;
uint32_t pauseStart = 0;

// manual control, one bit per pin
uint16_t flushMask = 0;
uint16_t testMask = 0;
uint32_t testStart[MAX_ACTIONS];

// ---- serial ----------------------------------------------------------------

enum ParseState : uint8_t
{
  P_SYNC1,
  P_SYNC2,
  P_CMD,
  P_SEQ,
  P_LEN,
  P_PAYLOAD,
  P_CRC
};

ParseState parseState = P_SYNC1;
uint8_t rxCmd = 0;
uint8_t rxSeq = 0;
uint8_t rxLen = 0;
uint8_t rxIndex = 0;
uint8_t rxCrc = 0;
uint8_t rxPayload[MAX_PAYLOAD];
uint32_t lastByteMs = 0;

// last answered request, to answer a repeated one without executing it again
uint8_t lastSeq = 0;
uint8_t lastCmd = 0;
uint8_t lastError = 0;

uint8_t crc8(uint8_t crc, uint8_t data)
{
  crc ^= data;
  for (uint8_t i = 0; i < 8; i++)
  {
    crc = (crc & 0x80) ? (uint8_t)((crc << 1) ^ 0x07) : (uint8_t)(crc << 1);
  }
  return crc;
}

void sendFrame(uint8_t cmd, uint8_t seq, const uint8_t *payload, uint8_t length)
{
  uint8_t frame[5 + MAX_PAYLOAD + 1];
  frame[0] = SYNC1;
  frame[1] = SYNC2;
  frame[2] = cmd;
  frame[3] = seq;
  frame[4] = length;
  uint8_t crc = crc8(crc8(crc8(0, cmd), seq), length);
  for (uint8_t i = 0; i < length; i++)
  {
    frame[5 + i] = payload[i];
    crc = crc8(crc, payload[i]);
  }
  frame[5 + length] = crc;
  Serial.write(frame, 6 + length);
}

void sendReply(uint8_t seq, uint8_t error)
{
  if (error == 0)
  {
    sendFrame(CMD_ACK, seq, NULL, 0);
  }
  else
  {
    sendFrame(CMD_NAK, seq, &error, 1);
  }
}

void sendHello(uint8_t seq)
{
  const uint8_t payload[3] = {PROTOCOL_VERSION, FIRMWARE_MAJOR, FIRMWARE_MINOR};
  sendFrame(CMD_HELLO_REPLY, seq, payload, 3);
}

uint16_t readU16(const uint8_t *p)
{
  return (uint16_t)p[0] | ((uint16_t)p[1] << 8);
}

uint32_t readU32(const uint8_t *p)
{
  return (uint32_t)p[0] | ((uint32_t)p[1] << 8) | ((uint32_t)p[2] << 16) | ((uint32_t)p[3] << 24);
}

// ---- outputs ---------------------------------------------------------------

bool validPin(uint8_t pin)
{
  return pin >= FIRST_OUTPUT_PIN && pin <= LAST_OUTPUT_PIN;
}

uint16_t pinBit(uint8_t pin)
{
  return (uint16_t)1 << (pin - FIRST_OUTPUT_PIN);
}

void allOff()
{
  for (uint8_t pin = FIRST_OUTPUT_PIN; pin <= LAST_OUTPUT_PIN; pin++)
  {
    digitalWrite(pin, LOW);
  }
  digitalWrite(ACTIVE_PIN, LOW);
  flushMask = 0;
  testMask = 0;
}

void updateTestPulses(uint32_t now)
{
  if (testMask == 0)
  {
    return;
  }
  for (uint8_t i = 0; i < MAX_ACTIONS; i++)
  {
    uint16_t bit = (uint16_t)1 << i;
    if ((testMask & bit) && (uint32_t)(now - testStart[i]) >= TEST_PULSE_US)
    {
      testMask &= ~bit;
      // a siphon that is being flushed stays open
      digitalWrite(FIRST_OUTPUT_PIN + i, (flushMask & bit) ? HIGH : LOW);
    }
  }
}

// ---- running ---------------------------------------------------------------

void finish(uint8_t status)
{
  allOff();
  runState = IDLE;
  sendFrame(CMD_FINISHED, 0, &status, 1);
}

void startCycle()
{
  uint8_t payload[4] = {(uint8_t)(cycle + 1), (uint8_t)((cycle + 1) >> 8), (uint8_t)cycles, (uint8_t)(cycles >> 8)};
  sendFrame(CMD_CYCLE, 0, payload, 4);
  // nothing is left to transmit while the actions are timed
  Serial.flush();

  remaining = actionCount;
  runState = CYCLE;
  uint32_t now = micros();
  for (uint8_t i = 0; i < actionCount; i++)
  {
    Action &a = actions[i];
    a.index = 0;
    a.state = WAITING;
    a.mark = now;
    a.wait = a.timings[0].delay + a.timings[0].delayIncrement * cycle;
  }
}

// Steps are chained from the scheduled time of the previous step, not from
// the time it was noticed, so the loop duration does not add up.
void updateAction(Action &a, uint32_t now)
{
  while (a.state != DONE && (uint32_t)(now - a.mark) >= a.wait)
  {
    a.mark += a.wait;
    if (a.state == WAITING)
    {
      const Timing &t = a.timings[a.index];
      a.wait = t.release + t.releaseIncrement * cycle;
      if (a.wait > 0)
      {
        digitalWrite(a.pin, HIGH);
        a.state = ACTIVE;
        continue;
      }
    }
    else
    {
      digitalWrite(a.pin, LOW);
    }
    a.index++;
    if (a.index >= a.count)
    {
      a.state = DONE;
      remaining--;
    }
    else
    {
      a.state = WAITING;
      a.wait = a.timings[a.index].delay + a.timings[a.index].delayIncrement * cycle;
    }
  }
}

void updateRun()
{
  if (runState == CYCLE)
  {
    uint32_t now = micros();
    for (uint8_t i = 0; i < actionCount; i++)
    {
      updateAction(actions[i], now);
    }
    if (remaining == 0)
    {
      cycle++;
      if (cycle >= cycles)
      {
        finish(FINISHED_DONE);
      }
      else
      {
        runState = CYCLE_PAUSE;
        pauseStart = millis();
      }
    }
  }
  else if (runState == CYCLE_PAUSE && (uint32_t)(millis() - pauseStart) >= cycleDelayMs)
  {
    startCycle();
  }
}

// ---- commands --------------------------------------------------------------

// Returns 0 or an error code.
uint8_t execute(uint8_t cmd, const uint8_t *p, uint8_t length)
{
  uint8_t expected;
  switch (cmd)
  {
  case CMD_BEGIN:
    expected = 6;
    break;
  case CMD_ACTION:
  case CMD_TEST:
    expected = 1;
    break;
  case CMD_TIMING:
    expected = 16;
    break;
  case CMD_FLUSH:
    expected = 2;
    break;
  case CMD_RUN:
  case CMD_CANCEL:
    expected = 0;
    break;
  default:
    return ERR_UNKNOWN_COMMAND;
  }
  if (length != expected)
  {
    return ERR_BAD_LENGTH;
  }

  if (cmd == CMD_CANCEL)
  {
    if (runState != IDLE)
    {
      finish(FINISHED_CANCELED);
    }
    else
    {
      allOff();
    }
    return 0;
  }
  if (runState != IDLE)
  {
    return ERR_BUSY;
  }

  switch (cmd)
  {
  case CMD_BEGIN:
  {
    if (readU16(p) == 0)
    {
      return ERR_BAD_VALUE;
    }
    cycles = readU16(p);
    cycleDelayMs = readU32(p + 2);
    actionCount = 0;
    return 0;
  }
  case CMD_ACTION:
  {
    if (!validPin(p[0]))
    {
      return ERR_BAD_PIN;
    }
    if (actionCount >= MAX_ACTIONS)
    {
      return ERR_TOO_MANY_ACTIONS;
    }
    actions[actionCount].pin = p[0];
    actions[actionCount].count = 0;
    actionCount++;
    return 0;
  }
  case CMD_TIMING:
  {
    if (actionCount == 0)
    {
      return ERR_NO_ACTION;
    }
    Action &a = actions[actionCount - 1];
    if (a.count >= MAX_TIMINGS)
    {
      return ERR_TOO_MANY_TIMINGS;
    }
    Timing &t = a.timings[a.count++];
    t.delay = readU32(p);
    t.release = readU32(p + 4);
    t.delayIncrement = readU32(p + 8);
    t.releaseIncrement = readU32(p + 12);
    return 0;
  }
  case CMD_RUN:
  {
    if (actionCount == 0)
    {
      return ERR_NO_ACTION;
    }
    for (uint8_t i = 0; i < actionCount; i++)
    {
      if (actions[i].count == 0)
      {
        return ERR_NO_ACTION;
      }
    }
    allOff();
    digitalWrite(ACTIVE_PIN, HIGH);
    cycle = 0;
    // startCycle() is called from the main loop after the ACK was sent
    runState = CYCLE_PAUSE;
    pauseStart = millis() - cycleDelayMs;
    return 0;
  }
  case CMD_TEST:
  {
    if (!validPin(p[0]))
    {
      return ERR_BAD_PIN;
    }
    testStart[p[0] - FIRST_OUTPUT_PIN] = micros();
    testMask |= pinBit(p[0]);
    digitalWrite(p[0], HIGH);
    return 0;
  }
  case CMD_FLUSH:
  {
    if (!validPin(p[0]))
    {
      return ERR_BAD_PIN;
    }
    if (p[1])
    {
      flushMask |= pinBit(p[0]);
      digitalWrite(p[0], HIGH);
    }
    else
    {
      flushMask &= ~pinBit(p[0]);
      if (!(testMask & pinBit(p[0])))
      {
        digitalWrite(p[0], LOW);
      }
    }
    return 0;
  }
  }
  return ERR_UNKNOWN_COMMAND;
}

void handleFrame()
{
  if (rxCmd == CMD_HELLO)
  {
    sendHello(rxSeq);
  }
  else
  {
    if (rxSeq != lastSeq || rxCmd != lastCmd)
    {
      lastError = execute(rxCmd, rxPayload, rxLen);
    }
    sendReply(rxSeq, lastError);
  }
  lastSeq = rxSeq;
  lastCmd = rxCmd;
}

void parse(uint8_t b)
{
  switch (parseState)
  {
  case P_SYNC1:
    if (b == SYNC1)
    {
      parseState = P_SYNC2;
    }
    break;
  case P_SYNC2:
    if (b == SYNC2)
    {
      parseState = P_CMD;
    }
    else if (b != SYNC1)
    {
      parseState = P_SYNC1;
    }
    break;
  case P_CMD:
    rxCmd = b;
    rxCrc = crc8(0, b);
    parseState = P_SEQ;
    break;
  case P_SEQ:
    rxSeq = b;
    rxCrc = crc8(rxCrc, b);
    parseState = P_LEN;
    break;
  case P_LEN:
    rxLen = b;
    rxIndex = 0;
    rxCrc = crc8(rxCrc, b);
    if (b > MAX_PAYLOAD)
    {
      // not a frame of ours, look for the next sync
      parseState = P_SYNC1;
    }
    else
    {
      parseState = b > 0 ? P_PAYLOAD : P_CRC;
    }
    break;
  case P_PAYLOAD:
    rxPayload[rxIndex++] = b;
    rxCrc = crc8(rxCrc, b);
    if (rxIndex >= rxLen)
    {
      parseState = P_CRC;
    }
    break;
  case P_CRC:
    parseState = P_SYNC1;
    if (b == rxCrc)
    {
      handleFrame();
    }
    else
    {
      // the sequence number may be damaged as well, so it is not remembered
      sendReply(rxSeq, ERR_CRC);
    }
    break;
  }
}

void readSerial()
{
  if (parseState != P_SYNC1 && (uint32_t)(millis() - lastByteMs) > FRAME_TIMEOUT_MS)
  {
    parseState = P_SYNC1;
  }
  while (Serial.available() > 0)
  {
    parse((uint8_t)Serial.read());
    lastByteMs = millis();
  }
}

// ---- main ------------------------------------------------------------------

void setup()
{
  // outputs first, so nothing floats or stays open after a reset
  for (uint8_t pin = FIRST_OUTPUT_PIN; pin <= LAST_OUTPUT_PIN; pin++)
  {
    digitalWrite(pin, LOW);
    pinMode(pin, OUTPUT);
  }
  digitalWrite(ACTIVE_PIN, LOW);
  pinMode(ACTIVE_PIN, OUTPUT);

  Serial.begin(19200, SERIAL_8N1);
  sendHello(0);
}

void loop()
{
  readSerial();
  updateRun();
  updateTestPulses(micros());
}

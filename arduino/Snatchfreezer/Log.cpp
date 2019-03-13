#include "Log.h"
#include "Constants.h"
#include "Arduino.h"

int logLevel = Log::DEBUG;
char buffer[40];

void Log::debug(uint8_t messageId) {
  if (DEBUG >= getLogLevel())
  {
    buffer[0] = SYNCBYTE1;
    buffer[1] = SYNCBYTE2;
    buffer[2] = COMMAND_LOG_DEBUG;
    buffer[3] = 1;
    buffer[4] = messageId;
    Serial.write(buffer, 5);
    Serial.flush();
  } 
  else
  {
    Log::sendDummy();
  }
}


void Log::info(uint8_t messageId) {
  if (INFO >= getLogLevel())
  {
    buffer[0] = SYNCBYTE1;
    buffer[1] = SYNCBYTE2;
    buffer[2] = COMMAND_LOG_INFO;
    buffer[3] = 1;
    buffer[4] = messageId;
    Serial.write(buffer, 5);
    Serial.flush();
  }
  else
  {
    Log::sendDummy();
  }
}

void Log::error(uint8_t messageId) {
  if (ERROR >= getLogLevel())
  {
    buffer[0] = SYNCBYTE1;
    buffer[1] = SYNCBYTE2;
    buffer[2] = COMMAND_LOG_ERROR;
    buffer[3] = 1;
    buffer[4] = messageId;
    Serial.write(buffer, 5);
    Serial.flush();
  }
  else
  {
    Log::sendDummy();
  }
}

void Log::sendDummy()
{
    buffer[0] = SYNCBYTE1;
    buffer[1] = SYNCBYTE2;
    buffer[2] = COMMAND_DUMMY;
    buffer[3] = 0;
    Serial.write(buffer, 4);
    Serial.flush();
}

void Log::echo(String s)
{
  buffer[0] = SYNCBYTE1;
  buffer[1] = SYNCBYTE2;
  buffer[2] = COMMAND_ECHO;
  buffer[3] = s.length();
  for (uint8_t i = 0; i < s.length(); i++)
    {
      buffer[4 + i] = { s.charAt(i) };
    }
  Serial.write(buffer, 4 + s.length());
  Serial.flush();
}

void Log::setLogLevel(int level)
{
  logLevel = level;
}

int Log::getLogLevel()
{
  return logLevel;
}

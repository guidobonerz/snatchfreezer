#include "Constants.h"
#include "Log.h"
#include "NumberConverter.h"
#include "Arduino.h"
#include "SerialHelper.h"

#define arraySize(x)  (sizeof(x) / sizeof((x)[0]))

struct ActionTimer {
	bool started = false;
	bool finished = false;
	uint8_t pin = 0;
	uint8_t timerCount = 0;
	uint8_t maxTimerCount = 0;
	uint32_t targetTime = 0;
	uint32_t delayStartTime[5] = { 0, 0, 0, 0, 0 };
	uint32_t releaseStartTime[5] = { 0, 0, 0, 0, 0 };
	uint32_t delayCycleIncrement[5] = { 0, 0, 0, 0, 0 };
	uint32_t releaseCycleIncrement[5] = { 0, 0, 0, 0, 0 };
} actionTimer[10];

uint8_t readDataBuffer[100];
uint8_t currentSerialCommand = 0;
uint8_t syncCount = 0;
uint8_t maxActionCount = 0;
uint8_t currentActionCount = 0;
uint8_t maxCycleCount = 0;
uint8_t currentCycleCount = 0;
uint8_t calculatedChecksum = 0;
uint8_t retries = 0;
uint8_t currentRow = 0;
uint32_t maxCycleDelay = 0;
uint32_t targetCycleDelayTime = 0;
uint32_t inc = 0;


int phase;
int serialReadPhase;
int completeCount = 0;
int dataIndex = 0;
int dataLength = 0;

bool runProgram = false;
bool nextCycle = true;
bool canceled = false;
bool pause = false;



void setup() {
	Serial.begin(57600);
	pinMode(ACTIVE_PIN, OUTPUT); //ControlLED
	pinMode(2, OUTPUT);
	serialReadPhase = READ_DATA_PREFIX;
	phase = PHASE_READ_SERIAL;
	dataIndex = 0;
	dataLength = 0;
	reset();
}

void clearActions()
{
	currentActionCount = 0;
	//currentCycleCount = 0;
	completeCount = 0;  
	for (int i = 0; i < arraySize(actionTimer); i++)
	{
		actionTimer[i].timerCount = 0;
		actionTimer[i].targetTime = 0; 
		actionTimer[i].finished = false;
		actionTimer[i].started = false;
	}
}

void  resetStartTime()
{
	uint32_t startMillis = millis();
	for (int i = 0; i < arraySize(actionTimer); i++)
	{
		actionTimer[i].targetTime =  startMillis + actionTimer[i].delayStartTime[0];
	}
}

void reset()
{
	while (Serial.read() != -1){};
	clearActions();
	maxActionCount = 0;
	currentCycleCount = 0;
	retries = 0;
	pause = false;
	runProgram = false;
	nextCycle = false;
}

void addAction()
{
	currentActionCount++;
	maxActionCount = currentActionCount;
	String s = String("MaxActionCount:");
	s += currentActionCount;
	Log::info(s);
}

void setActionPin(uint8_t pin)
{
	actionTimer[currentActionCount-1].pin = pin;
	pinMode(pin, OUTPUT);
	//digitalWrite(pin, HIGH);
}

void addActionTimings()
{
	actionTimer[currentActionCount-1].timerCount++;
	actionTimer[currentActionCount-1].maxTimerCount = actionTimer[currentActionCount-1].timerCount;
}
void setDelayTime(uint32_t delayTime)
{
	actionTimer[currentActionCount-1].delayStartTime[actionTimer[currentActionCount-1].timerCount-1] = delayTime;
}

void setReleaseTime(uint32_t releaseTime)
{
	actionTimer[currentActionCount-1].releaseStartTime[actionTimer[currentActionCount-1].timerCount-1] = releaseTime;
}

void setStartDelayIncrementTime(uint32_t delayCycleIncrement)
{
	actionTimer[currentActionCount-1].delayCycleIncrement[actionTimer[currentActionCount-1].timerCount-1] = delayCycleIncrement;
}

void setReleaseDelayIncrementTime(uint32_t releaseCycleIncrement)
{
	actionTimer[currentActionCount-1].releaseCycleIncrement[actionTimer[currentActionCount-1].timerCount-1] = releaseCycleIncrement;
}
void setCycleCount(uint8_t cycleCount)
{
	maxCycleCount = cycleCount;
}

void setCycleDelay(uint32_t cycleDelay)
{
	maxCycleDelay = cycleDelay;
}

void loop()
{
	switch (phase)
	{
	case PHASE_READ_SERIAL:
	{
		if (Serial && Serial.available() != 0)
		{
			uint8_t b = (uint8_t)Serial.read();
			parse(b);
			//delay(10);
		}

		break;
	}
	case PHASE_EXECUTE_COMMAND:
	{
		execute();
		break;
	}
	case PHASE_RUN_PROGRAM:
	{
		if (!nextCycle && millis() >= targetCycleDelayTime)
		{
			SerialHelper::setCycleCount(currentCycleCount + 1);
			nextCycle = true;
			currentActionCount = 0;
			completeCount = 0;
			clearActions();
			resetStartTime();
			digitalWrite(ACTIVE_PIN, HIGH);

		}
		if (nextCycle)
		{
			//-------------------------------------------------------
			for (int i = 0; i < maxActionCount; i++) {

				if (!actionTimer[i].finished)
				{
					if (!actionTimer[i].started && millis() >= actionTimer[i].targetTime)
					{
						actionTimer[i].started = true;
						inc = actionTimer[i].releaseCycleIncrement[actionTimer[i].timerCount] * currentCycleCount;
						actionTimer[i].targetTime = millis() + actionTimer[i].releaseStartTime[actionTimer[i].timerCount] + inc;

						digitalWrite(actionTimer[i].pin, HIGH);
					}

					if (actionTimer[i].started && millis() >= actionTimer[i].targetTime)
					{
						digitalWrite(actionTimer[i].pin, LOW);
						actionTimer[i].started = false;
						inc = actionTimer[i].delayCycleIncrement[actionTimer[i].timerCount] * currentCycleCount;
						actionTimer[i].targetTime = millis() + actionTimer[i].delayStartTime[actionTimer[i].timerCount] + inc;
						if (actionTimer[i].timerCount < actionTimer[i].maxTimerCount)
						{
							actionTimer[i].timerCount++;
						}
						else
						{
							actionTimer[i].finished = true;
							completeCount++;
						}
					}
				}
			}

			if (completeCount > maxActionCount-1)
			{
				targetCycleDelayTime = millis() + maxCycleDelay;
				nextCycle = false;
				completeCount = 0;
				currentCycleCount++;
			}

			//-------------------------------------------------------

		}
		

		if (currentCycleCount >= maxCycleCount)
		{
			currentCycleCount = 0;
			digitalWrite(ACTIVE_PIN, LOW);
			SerialHelper::finished();
			phase = PHASE_READ_SERIAL;
		}
		break;
	}
	}
}

void execute()
{
	String s = String("");
	uint32_t value = 0;
	if ((currentSerialCommand&PARAMETER_NUMBER) == PARAMETER_NUMBER)
	{
		switch (dataLength)
		{
		case BYTE:
		{
			value = NumberConverter::getUInt8(readDataBuffer, 0);
			Log::debug(String("Byte:") + String(value));
			break;
		}
		case WORD:
		{
			value = NumberConverter::getUInt16(readDataBuffer, 0);
			Log::debug(String("Word:") + String(value));
			break;
		}
		case DWORD:
		{
			value = NumberConverter::getUInt32(readDataBuffer, 0);
			Log::debug(String("DWord:") + String(value));
			break;
		}
		}

	}
	else if ((currentSerialCommand & PARAMETER_STRING) == PARAMETER_STRING)
	{
		for (int i = 0; i < dataLength; i++)
		{
			s += (char)readDataBuffer[i];
		}
		Log::debug("String Parameter:" + s);
	}
	else if ((currentSerialCommand & PARAMETER_NO) == PARAMETER_NO)
	{
		Log::debug("No Parameters");
	}

	switch (currentSerialCommand)
	{

	case COMMAND_RUN:
	{
		String s = String("Run");
		Log::info(s);
		runProgram = true;
		break;
	}
	case COMMAND_RESET:
	{
		Log::info("Reset");
		reset();
		break;
	}
	case COMMAND_CANCEL:
	{
		//Log::info("Cancel");
		//canceled = true;
		break;
	}
	case COMMAND_ECHO:
	{
		Log::info("Echo");
		Log::echo(s);
		break;
	}
	case COMMAND_SET_LOG_LEVEL:
	{
		Log::info("SetLogLevel");
		Log::setLogLevel(value);
		break;
	}
	case COMMAND_ADD_ACTION:
	{
		String s = String("AddAction");
		Log::info(s);
		addAction();
		break;
	}
	case COMMAND_SET_ACTION_PIN:
	{
		String s = String("SetActionPin:");
		s += value;
		Log::info(s);
		setActionPin(value);
		break;
	}
	case COMMAND_ADD_ACTION_TIMINGS:
	{
		String s = String("AddActionTimings");
		Log::info(s);
		addActionTimings();
		break;
	}
	case COMMAND_SET_ACTION_DELAY_TIME:
	{
		String s = String("SetActionDelay:");
		s += value;
		Log::info(s);
		setDelayTime(value);
		break;
	}
	case COMMAND_SET_ACTION_DELAY_INCREMENT_TIME:
	{
		String s = String("SetActionDelayIncrement:");
		s += value;
		Log::info(s);
		setStartDelayIncrementTime(value);
		break;
	}
	case COMMAND_SET_ACTION_RELEASE_TIME:
	{
		String s = String("SetActionRelease:");
		s += value;
		Log::info(s);
		setReleaseTime(value);
		break;
	}
	case COMMAND_SET_ACTION_RELEASE_INCREMENT_TIME:
	{
		String s = String("SetActionReleaseIncrement:");
		s += value;
		Log::info(s);
		setReleaseDelayIncrementTime(value);
		break;
	}
	case COMMAND_SET_CYCLE_COUNT:
	{
		String s = String("SetCycleCount:");
		s += value;
		Log::info(s);
		setCycleCount(value);
		break;
	}
	case COMMAND_SET_CYCLE_DELAY:
	{
		String s = String("SetCycleDelay:");
		s += value;
		Log::info(s);
		setCycleDelay(value);
		break;
	}
	}

	SerialHelper::sendNextCommand();
	serialReadPhase = READ_DATA_PREFIX;
	if (runProgram)
	{
		phase = PHASE_RUN_PROGRAM;
		runProgram = false;
		clearActions();
		resetStartTime();
	}
	else
	{
		phase = PHASE_READ_SERIAL;
	}

}

void parse(uint8_t data)
{
	switch (serialReadPhase) {
	case READ_DATA_PREFIX:
	{
		uint8_t b = data;
		calculatedChecksum ^= b;

		switch (b) {
		case SYNCBYTE1:
		{
			serialReadPhase = READ_DATA_PREFIX;
			Log::debug("Sync1");
			++syncCount;
			break;
		}
		case SYNCBYTE2:
		{
			Log::debug("Sync2");
			++syncCount;
			if (syncCount == 2) {
				serialReadPhase = READ_COMMAND;
				syncCount = 0;
			}
			break;
		}
		}
		break;
	}

	case READ_COMMAND:
	{
		Log::debug("Read Command");
		currentSerialCommand = data;
		calculatedChecksum ^= (uint8_t)currentSerialCommand;
		dataLength = 0;
		dataIndex = 0;
		serialReadPhase = READ_LENGTH;
		switch (currentSerialCommand)
		{
		case COMMAND_ADD_ACTION:
		case COMMAND_ADD_ACTION_TIMINGS:
		case COMMAND_RUN:
		case COMMAND_RESET:
		case COMMAND_CANCEL:
		case COMMAND_FINISHED:
		case COMMAND_NEXT:
		case COMMAND_REPEAT:
		{
			serialReadPhase = READ_DATA;
			break;
		}

		}
		break;
	}
	case READ_LENGTH:
	{
		String s = String("Read Length:");
		s += data;
		Log::debug(s);
		dataLength = data;
		dataIndex = 0;
		calculatedChecksum ^= (uint8_t)dataLength;
		serialReadPhase = READ_DATA;
		break;
	}
	case READ_DATA:
	{
		serialReadPhase = READ_DATA;
		if (dataIndex < dataLength)
		{
			String s = String("Read Data:");
			s += data;
			Log::debug(s);
			uint8_t b = data;
			calculatedChecksum ^= b;
			readDataBuffer[dataIndex++] = b;
		}
		else
		{
			if (retries < 3)
			{
				Log::debug("Read Checksum");

				if (data != calculatedChecksum)
				{
					Log::debug("Checksum ERROR");
					retries++;
					SerialHelper::repeatCommand();
				}
				else
				{
					Log::debug("Checksum OK");
					retries = 0;
					calculatedChecksum = 0;
					phase = PHASE_EXECUTE_COMMAND;
				}
			}
			else
			{
				SerialHelper::cancel();
			}
		}
		break;
	}

	}
}

/*
int freeRam() {
extern int __heap_start, *__brkval;
int v;
return (int)&v - (__brkval == 0 ? (int)&__heap_start : (int)__brkval);
}
*/



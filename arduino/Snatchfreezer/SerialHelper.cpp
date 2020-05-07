#include "Arduino.h"
#include "SerialHelper.h"
#include "Constants.h"

uint8_t shBuffer[10];

void SerialHelper::sendNextCommand()
{
	shBuffer[0] = SYNCBYTE1;
	shBuffer[1] = SYNCBYTE2;
	shBuffer[2] = COMMAND_NEXT;
	shBuffer[3] = 0;
	Serial.write(shBuffer, 4);
	Serial.flush();
}

void SerialHelper::repeatCommand()
{
	shBuffer[0] = SYNCBYTE1;
	shBuffer[1] = SYNCBYTE2;
	shBuffer[2] = COMMAND_REPEAT;
	shBuffer[3] = 0;
	Serial.write(shBuffer, 4);
	Serial.flush();
}
void SerialHelper::cancel()
{
	shBuffer[0] = SYNCBYTE1;
	shBuffer[1] = SYNCBYTE2;
	shBuffer[2] = COMMAND_CANCEL;
	shBuffer[3] = 0;
	Serial.write(shBuffer, 4);
	Serial.flush();
}

void SerialHelper::finished()
{
	shBuffer[0] = SYNCBYTE1;
	shBuffer[1] = SYNCBYTE2;
	shBuffer[2] = COMMAND_FINISHED;
	shBuffer[3] = 0;
	Serial.write(shBuffer, 4);
	Serial.flush();
}

void SerialHelper::setCycleCount(uint8_t count)
{
	shBuffer[0] = SYNCBYTE1;
	shBuffer[1] = SYNCBYTE2;
	shBuffer[2] = COMMAND_SET_CYCLE_COUNT;
	shBuffer[3] = 1;
	shBuffer[4] = count;
	Serial.write(shBuffer, 5);
	//Serial.flush();
}

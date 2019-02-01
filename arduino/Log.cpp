#include "Log.h"
#include "Constants.h"
#include "Arduino.h"

int logLevel=Log::DEBUG;
uint8_t buffer[40];

void Log::debug(String s) {
	if (DEBUG >= getLogLevel())
	{
		buffer[0] = SYNCBYTE1;
		buffer[1] = SYNCBYTE2;
		buffer[2] = COMMAND_LOG_DEBUG;
		buffer[3] = s.length();
		for (uint8_t i = 0; i < s.length(); i++)
		{
			buffer[4 + i] = { s.charAt(i) };
		}
		Serial.write(buffer, 4 + s.length());
		//Serial.flush();
	}
	
}
void Log::info(String s) {
	
	if (INFO >= getLogLevel())
	{
		buffer[0] = SYNCBYTE1;
		buffer[1] = SYNCBYTE2;
		buffer[2] = COMMAND_LOG_INFO;
		buffer[3] = s.length();
		for (uint8_t i = 0; i < s.length(); i++)
		{
			buffer[4 + i] = { s.charAt(i) };
		}
		Serial.write(buffer, 4 + s.length());
		//Serial.flush();
	}
	
}
void Log::error(String s) {
	if (ERROR>=getLogLevel())
	{
		buffer[0] = SYNCBYTE1;
		buffer[1] = SYNCBYTE2;
		buffer[2] = COMMAND_LOG_ERROR;
		buffer[3] = s.length();
		for (uint8_t i = 0; i < s.length(); i++)
		{
			buffer[4 + i] = { s.charAt(i) };
		}
		Serial.write(buffer, 4 + s.length());
		//Serial.flush();
	}
	
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
	//Serial.flush();
}

void Log::setLogLevel(int level)
{
	logLevel = level;
	String s = String("LogLevel:"+getLogLevel());
	echo(s);
}

int Log::getLogLevel()
{
	return logLevel;
}
#pragma once
#include "Arduino.h"
class Log
{
//private:
	//static int logLevel;
	//static uint8_t buffer[];
	
public:
	static const int DEBUG = 0;
	static const int INFO = 1;
	static const int ERROR = 2;
	static const int OFF = 99;

	static void setLogLevel(int level);
	static int getLogLevel();
	static void debug(String s);
	static void info(String s);
	static void error(String s);
	static void echo(String s);
};



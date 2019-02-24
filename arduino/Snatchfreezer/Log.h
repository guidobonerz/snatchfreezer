#pragma once
#include "Arduino.h"
class Log
{
	
public:
	static const int DEBUG = 1;
	static const int INFO = 2;
	static const int ERROR = 3;
  static const int OFF = 99;
	

	static void setLogLevel(int level);
	static int getLogLevel();
	static void debug(uint8_t messageId);
  static void info(uint8_t messageId);
	static void error(uint8_t messageId);
	static void echo(String s);
};

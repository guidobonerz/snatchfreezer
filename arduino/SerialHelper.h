#pragma once
class SerialHelper
{

public:
	
	static void sendNextCommand();
	static void repeatCommand();
	static void cancel();
	static void setCycleCount(uint8_t count);
	static void finished();
};


#pragma once
#include "Arduino.h"
class NumberConverter
{
public: 
	 static uint32_t getUInt32(uint8_t *buffer,uint16_t offset);
	 static uint16_t getUInt16(uint8_t *buffer, uint16_t offset);
	 static uint8_t getUInt8(uint8_t *buffer, uint16_t offset);
};


#include "NumberConverter.h"

uint32_t NumberConverter::getUInt32(uint8_t *buffer, uint16_t offset)
{
	uint32_t value = (((uint32_t)(buffer[offset] & 0xff)) | ((uint32_t)(buffer[offset + 1] & 0xff)) << 8 | ((uint32_t)(buffer[offset + 2] & 0xff)) << 16 | ((uint32_t)(buffer[offset + 3] & 0xff)) << 24) & 0xffffffff;
	return value;
}

uint16_t NumberConverter::getUInt16(uint8_t *buffer, uint16_t offset)
{
	uint16_t value = (((uint16_t)(buffer[offset] & 0xff)) | ((uint16_t)(buffer[offset + 1] & 0xff)) << 8) & 0xffff;
	return value;
}

uint8_t NumberConverter::getUInt8(uint8_t *buffer, uint16_t offset)
{
	uint8_t value = (uint8_t)(buffer[offset] & 0xff);
	return value;
}

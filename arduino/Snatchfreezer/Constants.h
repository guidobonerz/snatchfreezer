 #pragma once
#include "Arduino.h"
const int PARAMETER_NO = B00000000;
const int PARAMETER_STRING = B10000000;
const int PARAMETER_NUMBER = B11000000;
const int SYNCBYTE1 = 0xaa;
const int SYNCBYTE2 = 0x55;
const int COMMAND_SET_ACTION_PIN                    = B11000001;
const int COMMAND_SET_ACTION_DELAY_TIME             = B11000010;
const int COMMAND_SET_ACTION_RELEASE_TIME           = B11000011;
const int COMMAND_SET_ACTION_DELAY_INCREMENT_TIME   = B11000100;
const int COMMAND_SET_ACTION_RELEASE_INCREMENT_TIME = B11000101;
const int COMMAND_SET_CYCLE_COUNT                   = B11000110;
const int COMMAND_SET_CYCLE_DELAY                   = B11000111;
const int COMMAND_RUN                               = B11001001;
const int COMMAND_SEND_RESPONSE                     = B11001011;
const int COMMAND_ADD_ACTION                        = B00000001;
const int COMMAND_ADD_ACTION_TIMINGS                = B00000010;
const int COMMAND_NEXT                              = B00000011;
const int COMMAND_REPEAT                            = B00000100;
const int COMMAND_SET_LOG_LEVEL                     = B11001011;
const int COMMAND_CANCEL                            = B00000111;
const int COMMAND_RESET                             = B00001000;
const int COMMAND_FINISHED                          = B00001001;
const int COMMAND_DUMMY                             = B00111111;
const int COMMAND_ECHO                              = B10001001;
const int COMMAND_LOG_INFO                          = B11001010;
const int COMMAND_LOG_DEBUG                         = B11001011;
const int COMMAND_LOG_ERROR                         = B11001100;
const int COMMAND_LOG_OFF                           = B10001110;

const int SHOT = 0;
const int FLUSH_ON = 1;
const int FLUSH_OFF = 2;
const int TEST = 3;
const int HELO = 4;

const int MESSAGE_BYTE=1;
const int MESSAGE_WORD=2;
const int MESSAGE_DWORD=3;
const int MESSAGE_STRING_PARAMETER=4;
const int MESSAGE_NUMBER_PARAMETER=5;
const int MESSAGE_NO_PARAMETER=6;
const int MESSAGE_RUN=7;
const int MESSAGE_RESET=8;
const int MESSAGE_CANCEL=9;
const int MESSAGE_ECHO=10;
const int MESSAGE_SET_LOG_LEVEL=11;
const int MESSAGE_ADD_ACTION=12;
const int MESSAGE_SET_ACTION_PIN=13;
const int MESSAGE_ADD_ACTION_TIMIMGS=14;
const int MESSAGE_SET_ACTION_DELAY=15;
const int MESSAGE_SET_ACTION_DELAY_INCREMENT=16;
const int MESSAGE_SET_ACTION_RELEASE=17;
const int MESSAGE_SET_ACTION_RELEASE_INCREMENT=18;
const int MESSAGE_SET_CYCLE_COUNT=19;
const int MESSAGE_SET_CYCLE_DELAY=20;
const int MESSAGE_SYNC1=21;
const int MESSAGE_SYNC2=22;
const int MESSAGE_READ_COMMAND=23;
const int MESSAGE_READ_LENGTH=24;
const int MESSAGE_READ_DATA=25;
const int MESSAGE_READ_CHECKSUM=26;
const int MESSAGE_CHECKSUM_ERROR=27;
const int MESSAGE_CHECKSUM_OK=28;
const int MESSAGE_MAX_ACTION_COUNT=29;
const int MESSAGE_DUMMY=99;




//const int COMMAND_RUN = 10;

const int READ_DATA_PREFIX = 10;
const int READ_COMMAND = 20;
const int READ_LENGTH = 30;
const int READ_DATA = 40;
const int READ_CHECKSUM = 50;
const int EXECUTE_COMMAND = 60;


const int CAMERA1_PIN = 10;
const int CAMERA2_PIN = 11;
const int FLASH1_PIN = 8;
const int FLASH2_PIN = 9;
const int VALVE1_PIN = 2;
const int VALVE2_PIN = 3;
const int VALVE3_PIN = 4;
const int VALVE4_PIN = 5;
const int VALVE5_PIN = 6;
const int VALVE6_PIN = 7;
//const int BUTTON_PIN = 1;// unset
const int ACTIVE_PIN = 13;
const int INACTIVE = 100;

const int PHASE_READ_SERIAL = 10;
const int PHASE_EXECUTE_COMMAND = 20;
const int PHASE_RUN_PROGRAM = 30;

const uint8_t COMMAND_WAIT_FOR_SERIAL_DATA = 0;
const uint8_t COMMAND_PARSE_BUFFER = 1;

const uint8_t BYTE = 1;
const uint8_t WORD = 2;
const uint8_t DWORD = 4;

package de.drazil.snatchfreezer;

public class Constants {
	
	public final static int MAX_ACTUATOR_TIMING_SETS = 5;
	public final static int PARAMETER_NO = 0b00000000;
	public final static int PARAMETER_STRING = 0b10000000;
	public final static int PARAMETER_NUMBER = 0b11000000;
	public final static int SYNCBYTE1 = 0xaa;
	public final static int SYNCBYTE2 = 0x55;
	public final static int COMMAND_SET_ACTION_PIN = 0b11000001;
	public final static int COMMAND_SET_ACTION_DELAY_TIME = 0b11000010;
	public final static int COMMAND_SET_ACTION_RELEASE_TIME = 0b11000011;
	public final static int COMMAND_SET_ACTION_DELAY_INCREMENT_TIME = 0b11000100;
	public final static int COMMAND_SET_ACTION_RELEASE_INCREMENT_TIME = 0b11000101;
	public final static int COMMAND_SET_CYCLE_COUNT = 0b11000110;
	public final static int COMMAND_SET_CYCLE_DELAY = 0b11000111;
	public final static int COMMAND_FLUSH = 0b11001001;
	public final static int COMMAND_TEST = 0b11001010;
	public final static int COMMAND_ADD_ACTION = 0b00000001;
	public final static int COMMAND_ADD_ACTION_TIMINGS = 0b00000010;
	public final static int COMMAND_NEXT = 0b00000011;
	public final static int COMMAND_REPEAT = 0b00000100;
	public final static int COMMAND_SET_LOG_LEVEL = 0b11001011;
	public final static int COMMAND_RUN = 0b00000110;
	public final static int COMMAND_CANCEL = 0b00000111;
	public final static int COMMAND_RESET = 0b00001000;
	public final static int COMMAND_FINISHED = 0b00001001;
	public final static int COMMAND_DUMMY = 0b00111111;
	public final static int COMMAND_ECHO = 0b10001001;
	public final static int COMMAND_LOG_INFO = 0b11001010;
	public final static int COMMAND_LOG_DEBUG = 0b11001011;
	public final static int COMMAND_LOG_ERROR = 0b11001100;
	public final static int COMMAND_LOG_OFF = 0b10001110;

	public final static int MESSAGE_BYTE = 1;
	public final static int MESSAGE_WORD = 2;
	public final static int MESSAGE_DWORD = 3;
	public final static int MESSAGE_STRING_PARAMETER = 4;
	public final static int MESSAGE_NUMBER_PARAMETER = 5;
	public final static int MESSAGE_NO_PARAMETER = 6;
	public final static int MESSAGE_RUN = 7;
	public final static int MESSAGE_RESET = 8;
	public final static int MESSAGE_CANCEL = 9;
	public final static int MESSAGE_ECHO = 10;
	public final static int MESSAGE_SET_LOG_LEVEL = 11;
	public final static int MESSAGE_ADD_ACTION = 12;
	public final static int MESSAGE_SET_ACTION_PIN = 13;
	public final static int MESSAGE_ADD_ACTION_TIMIMGS = 14;
	public final static int MESSAGE_SET_ACTION_DELAY = 15;
	public final static int MESSAGE_SET_ACTION_DELAY_INCREMENT = 16;
	public final static int MESSAGE_SET_ACTION_RELEASE = 17;
	public final static int MESSAGE_SET_ACTION_RELEASE_INCREMENT = 18;
	public final static int MESSAGE_SET_CYCLE_COUNT = 19;
	public final static int MESSAGE_SET_CYCLE_DELAY = 20;
	public final static int MESSAGE_SYNC1 = 21;
	public final static int MESSAGE_SYNC2 = 22;
	public final static int MESSAGE_READ_COMMAND = 23;
	public final static int MESSAGE_READ_LENGTH = 24;
	public final static int MESSAGE_READ_DATA = 25;
	public final static int MESSAGE_READ_CHECKSUM = 26;
	public final static int MESSAGE_CHECKSUM_ERROR = 27;
	public final static int MESSAGE_CHECKSUM_OK = 28;
	public final static int MESSAGE_MAX_ACTION_COUNT = 29;

	public final static int READ_DATA_PREFIX = 10;
	public final static int READ_COMMAND = 20;
	public final static int READ_LENGTH = 30;
	public final static int READ_DATA = 40;
	public final static int EXECUTE_COMMAND = 50;

	public final static int DEBUG = 1;
	public final static int INFO = 2;
	public final static int ERROR = 3;
	public final static int OFF = 99;
}

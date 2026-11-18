#pragma once
#include "Arduino.h"

// Serial protocol between the frontend and the controller (19200 baud, 8N1).
// Keep in sync with frontend/src/protocol.rs.
//
// Frame:  SYNC1 SYNC2 CMD SEQ LEN PAYLOAD[LEN] CRC
//   CRC   CRC-8 (polynomial 0x07, init 0) over CMD, SEQ, LEN and PAYLOAD
//   SEQ   1..255 for requests, echoed in the reply; 0 for unsolicited events
//   Numbers are little endian.
//
// Every request is answered with exactly one ACK, NAK or HELLO_REPLY. The
// frontend repeats a request (same SEQ) if no answer arrives in time; a
// repeated request is answered again but not executed twice.

const uint8_t PROTOCOL_VERSION = 2;
const uint8_t FIRMWARE_MAJOR = 2;
const uint8_t FIRMWARE_MINOR = 0;

const uint8_t SYNC1 = 0xaa;
const uint8_t SYNC2 = 0x55;
const uint8_t MAX_PAYLOAD = 32;

// frontend -> controller
const uint8_t CMD_HELLO = 0x01;  // -
const uint8_t CMD_BEGIN = 0x10;  // cycles u16, cycle delay (ms) u32; clears the program
const uint8_t CMD_ACTION = 0x11; // pin u8; starts a new action (one table)
const uint8_t CMD_TIMING = 0x12; // delay, release, delay increment, release increment (us) u32 each
const uint8_t CMD_RUN = 0x13;    // -
const uint8_t CMD_CANCEL = 0x14; // -; stops everything and switches all outputs off
const uint8_t CMD_TEST = 0x20;   // pin u8; short pulse
const uint8_t CMD_FLUSH = 0x21;  // pin u8, on u8

// controller -> frontend
const uint8_t CMD_ACK = 0x80;         // -
const uint8_t CMD_NAK = 0x81;         // error code u8
const uint8_t CMD_HELLO_REPLY = 0x82; // protocol version u8, firmware major u8, minor u8
const uint8_t CMD_CYCLE = 0x90;       // event: current cycle (1 based) u16, cycles u16
const uint8_t CMD_FINISHED = 0x91;    // event: 0 = done, 1 = canceled

const uint8_t ERR_CRC = 1;
const uint8_t ERR_UNKNOWN_COMMAND = 2;
const uint8_t ERR_BAD_LENGTH = 3;
const uint8_t ERR_BAD_PIN = 4;
const uint8_t ERR_TOO_MANY_ACTIONS = 5;
const uint8_t ERR_TOO_MANY_TIMINGS = 6;
const uint8_t ERR_BUSY = 7;
const uint8_t ERR_NO_ACTION = 8;
const uint8_t ERR_BAD_VALUE = 9;

const uint8_t FINISHED_DONE = 0;
const uint8_t FINISHED_CANCELED = 1;

// Hardware
const uint8_t FIRST_OUTPUT_PIN = 2; // valves 2..7, flashes 8..9, cameras 10..11
const uint8_t LAST_OUTPUT_PIN = 11;
const uint8_t ACTIVE_PIN = 13; // control LED

const uint8_t MAX_ACTIONS = LAST_OUTPUT_PIN - FIRST_OUTPUT_PIN + 1;
const uint8_t MAX_TIMINGS = 5;

const uint32_t TEST_PULSE_US = 50000;
// A frame whose bytes arrive further apart than this is dropped.
const uint16_t FRAME_TIMEOUT_MS = 100;

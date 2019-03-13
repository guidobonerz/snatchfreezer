#pragma once
#include "Arduino.h"
#include "NumberConverter.h"

class Action
{
private:
	bool delayPassed;
	bool releasePassed;
	uint8_t pin;
	uint8_t timerCount;
	uint8_t maxTimerCount;
	uint32_t _delayStartTime[5];
	uint32_t _releaseStartTime[5];
	uint32_t _delayCycleIncrement[5];
	uint32_t _releaseCycleIncrement[5];

	uint32_t targetTime;
	
public:
	Action(void);
	Action(uint8_t pinNumber);
	~Action()
	{
		
		delete[] _delayStartTime;
		delete[] _releaseStartTime;
		delete[] _delayCycleIncrement;
		delete[] _releaseCycleIncrement;
		
	};
	void init(uint32_t startTime);
	bool isRunning(void);
	void reset(void);
	void addTiming(uint32_t delayStartTime, uint32_t releaseStartTime);
};


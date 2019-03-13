#include "Action.h"

Action::Action(void)
{}

Action::Action(uint8_t pinNumber)
{
	pin = pinNumber;
	pinMode(pin, OUTPUT);
}

/*
Action::~Action()
{
	
}
*/

void Action::init(uint32_t startTime)
{
	reset();
	targetTime = _delayStartTime[timerCount] + startTime;
}

void Action::reset()
{
	timerCount = 0;
	maxTimerCount = 0;
	delayPassed = false;
	releasePassed = false;
}

bool Action::isRunning()
{
	if (!releasePassed)
	{
		if (!delayPassed && micros() >=targetTime)
		{
			delayPassed = true;
			targetTime = micros() + _releaseStartTime[timerCount];
			digitalWrite(pin, HIGH);
		}

		if (delayPassed && micros() >= targetTime)
		{
			digitalWrite(pin, LOW);
			delayPassed = true;
			
			if (timerCount < maxTimerCount)
			{
				timerCount++;
				targetTime = micros() + _delayStartTime[timerCount];
			}
			else
			{
				releasePassed = true;
			}
		}
	}
	return !releasePassed;
}

void Action::addTiming(uint32_t delayStartTime, uint32_t releaseStartTime)
{
	_delayStartTime[maxTimerCount] = delayStartTime;
	_releaseStartTime[maxTimerCount] = releaseStartTime;
	maxTimerCount++;
}


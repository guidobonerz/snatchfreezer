package de.drazil.util;

import static de.drazil.util.Constants.COMMAND_ADD_ACTION;
import static de.drazil.util.Constants.COMMAND_ADD_ACTION_TIMINGS;
import static de.drazil.util.Constants.COMMAND_CANCEL;
import static de.drazil.util.Constants.COMMAND_ECHO;
import static de.drazil.util.Constants.COMMAND_RESET;
import static de.drazil.util.Constants.COMMAND_RUN;
import static de.drazil.util.Constants.COMMAND_SET_ACTION_DELAY_INCREMENT_TIME;
import static de.drazil.util.Constants.COMMAND_SET_ACTION_DELAY_TIME;
import static de.drazil.util.Constants.COMMAND_SET_ACTION_PIN;
import static de.drazil.util.Constants.COMMAND_SET_ACTION_RELEASE_INCREMENT_TIME;
import static de.drazil.util.Constants.COMMAND_SET_ACTION_RELEASE_TIME;
import static de.drazil.util.Constants.COMMAND_SET_CYCLE_COUNT;
import static de.drazil.util.Constants.COMMAND_SET_CYCLE_DELAY;
import static de.drazil.util.Constants.COMMAND_SET_LOG_LEVEL;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ConfigurationBuilder {
	private List<byte[]> commandList = null;

	public ConfigurationBuilder() {
		commandList = new ArrayList<byte[]>();
	}

	public void addEcho(String text) {
		byte[] ba = NumericConverter.toByteArray(COMMAND_ECHO, 1);
		ba = ArrayUtil.add(ba, NumericConverter.toByteArray((byte) text.length(), 1));
		ba = ArrayUtil.add(ba, ArrayUtil.getByteArrayFromString(text));
		commandList.add(createCommand(ba));
	}

	public void addRun(int mode) {
		//commandList.add(createCommand(NumericConverter.toByteArray(COMMAND_RUN, 1)));
		byte[] ba = NumericConverter.toByteArray(COMMAND_RUN, 1);
		ba = ArrayUtil.add(ba, NumericConverter.toByteArray((byte) 2));
		ba = ArrayUtil.add(ba, NumericConverter.toByteArray(mode, 2));
		commandList.add(createCommand(ba));
	}

	public void sendCancel() {
		commandList.add(createCommand(NumericConverter.toByteArray(COMMAND_CANCEL, 1)));
	}

	public void addReset() {
		commandList.add(createCommand(NumericConverter.toByteArray(COMMAND_RESET, 1)));
	}

	public void addSetLogLevel(int logLevel) {
		byte[] ba = NumericConverter.toByteArray(COMMAND_SET_LOG_LEVEL, 1);
		ba = ArrayUtil.add(ba, new byte[] { 1 });
		ba = ArrayUtil.add(ba, NumericConverter.toByteArray(logLevel, 1));
		commandList.add(createCommand(ba));
	}

	public void addAction(int pin) {
		commandList.add(createCommand(NumericConverter.toByteArray(COMMAND_ADD_ACTION, 1)));
		byte[] ba = NumericConverter.toByteArray(COMMAND_SET_ACTION_PIN, 1);
		ba = ArrayUtil.add(ba, NumericConverter.toByteArray((byte) 1));
		ba = ArrayUtil.add(ba, NumericConverter.toByteArray(pin, 1));
		commandList.add(createCommand(ba));
	}

	public void addActionTimings(long startDelay, long releaseDelay, long delayIncrement, long releaseIncrement) {
		commandList.add(createCommand(NumericConverter.toByteArray(COMMAND_ADD_ACTION_TIMINGS, 1)));
		byte ba[] = NumericConverter.toByteArray(COMMAND_SET_ACTION_DELAY_TIME, 1);
		ba = ArrayUtil.add(ba, NumericConverter.toByteArray((byte) 4));
		ba = ArrayUtil.add(ba, NumericConverter.toByteArray(startDelay, 4));
		commandList.add(createCommand(ba));
		ba = NumericConverter.toByteArray(COMMAND_SET_ACTION_RELEASE_TIME, 1);
		ba = ArrayUtil.add(ba, NumericConverter.toByteArray((byte) 4));
		ba = ArrayUtil.add(ba, NumericConverter.toByteArray(releaseDelay, 4));
		commandList.add(createCommand(ba));
		ba = NumericConverter.toByteArray(COMMAND_SET_ACTION_DELAY_INCREMENT_TIME, 1);
		ba = ArrayUtil.add(ba, NumericConverter.toByteArray((byte) 4));
		ba = ArrayUtil.add(ba, NumericConverter.toByteArray(delayIncrement, 4));
		commandList.add(createCommand(ba));
		ba = NumericConverter.toByteArray(COMMAND_SET_ACTION_RELEASE_INCREMENT_TIME, 1);
		ba = ArrayUtil.add(ba, NumericConverter.toByteArray((byte) 4));
		ba = ArrayUtil.add(ba, NumericConverter.toByteArray(releaseIncrement, 4));
		commandList.add(createCommand(ba));
	}

	public void addCycleCount(int cycleCount) {
		byte[] ba = NumericConverter.toByteArray(COMMAND_SET_CYCLE_COUNT, 1);
		ba = ArrayUtil.add(ba, NumericConverter.toByteArray((byte) 1));
		ba = ArrayUtil.add(ba, NumericConverter.toByteArray(cycleCount, 1));
		commandList.add(createCommand(ba));
	}

	public void addCycleDelay(long cycleDelay) {
		byte[] ba = NumericConverter.toByteArray(COMMAND_SET_CYCLE_DELAY, 1);
		ba = ArrayUtil.add(ba, NumericConverter.toByteArray((byte) 4));
		ba = ArrayUtil.add(ba, NumericConverter.toByteArray(cycleDelay, 4));
		commandList.add(createCommand(ba));
	}

	private byte[] createCommand(byte byteArray[]) {
		byte[] ba = ArrayUtil.add(new byte[] { (byte) 0xaa, (byte) 0x55 }, byteArray);
		ba = ArrayUtil.add(ba, new byte[] { ArrayUtil.getChecksum(ba) });
		return ba;
	}

	public int getCommandListSize() {
		return commandList.size();
	}

	public byte[] getCommand(int i) {
		return commandList.get(i);
	}

	public int indexOfCommand(byte commandBuffer[]) {
		return commandList.indexOf(commandBuffer);
	}

	public Iterator<byte[]> getIterator() {
		return commandList.iterator();
	}

	public void reset() {
		commandList = new ArrayList<byte[]>();
	}
}

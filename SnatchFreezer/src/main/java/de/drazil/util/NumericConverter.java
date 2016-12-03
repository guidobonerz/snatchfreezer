package de.drazil.util;

public class NumericConverter {

	public final static byte[] toByteArray(byte value) {
		return _toByteArray(value, 1);
	}

	public final static byte[] toByteArray(short value) {
		return _toByteArray(value, 2);
	}

	public final static byte[] toByteArray(short value, int size) {
		return _toByteArray(value, size);
	}

	public final static byte[] toByteArray(int value) {
		return _toByteArray(value, 4);
	}

	public final static byte[] toByteArray(int value, int size) {
		return _toByteArray(value, size);
	}

	public final static byte[] toByteArray(long value) {
		return _toByteArray(value, 8);
	}

	public final static byte[] toByteArray(long value, int size) {
		return _toByteArray(value, size);
	}

	private final static byte[] _toByteArray(long value, int size) {
		byte ba[] = new byte[size];

		for (int shift = 0, i = 0; shift < size * 8; shift += 8, i++) {
			ba[i] = (byte) ((value >> shift) & 0xff);
		}
		return ba;
	}

	public final static long toLong(byte buffer[]) {
		return (long) (buffer[0] & 0xff) + ((buffer[1] & 0xff) << 8)
				+ ((buffer[2] & 0xff) << 16) + ((buffer[3] & 0xff) << 24);
	}

	public final static int toInt(byte buffer[]) {
		return (int) (buffer[0] & 0xff) + ((buffer[1] & 0xff) << 8);
	}

	public final static byte toByte(byte buffer[]) {
		return (byte) (buffer[0] & 0xff);
	}
}

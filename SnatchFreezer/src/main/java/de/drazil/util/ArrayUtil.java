package de.drazil.util;

public class ArrayUtil
{
	public final static byte[] add(byte oldArray[], byte newArray[])
	{
		byte targetArray[] = new byte[oldArray.length + newArray.length];
		System.arraycopy(oldArray, 0, targetArray, 0, oldArray.length);
		System.arraycopy(newArray, 0, targetArray, oldArray.length, newArray.length);
		return targetArray;
	}

	public final static byte[] getByteArrayFromString(String text)
	{
		byte ba[] = new byte[text.length()];
		for (int i = 0; i < text.length(); i++)
			ba[i] = (byte) text.charAt(i);
		return ba;
	}

	public final static byte getChecksum(byte[] ba)
	{
		byte b = 0;
		for (int i = 0; i < ba.length; i++)
		{
			b ^= ba[i];
		}
		return b;
	}
}

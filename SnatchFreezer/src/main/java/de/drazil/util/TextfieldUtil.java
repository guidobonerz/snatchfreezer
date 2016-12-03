package de.drazil.util;

import javafx.scene.control.TextField;

public class TextfieldUtil
{
	public final static long getLongValue(TextField textfield)
	{
		String text = textfield.getText();
		if (text == null || text.equals(""))
		{
			return 0;
		}
		else
		{
			return Long.parseLong(text);
		}
	}

	public final static int getIntegerValue(TextField textfield)
	{
		String text = textfield.getText();
		if (text == null || text.equals(""))
		{
			return 0;
		}
		else
		{
			return Integer.parseInt(text);
		}
	}
}

package de.drazil.util;

import javafx.util.converter.LongStringConverter;

public class Long2StringConverter extends LongStringConverter {

	@Override
	public Long fromString(final String value) {
		Long l = value.isEmpty() || !isNumber(value) ? null : super.fromString(value);
		return l;
	}

	public boolean isNumber(String value) {
		int size = value.length();
		for (int i = 0; i < size; i++) {
			if (!Character.isDigit(value.charAt(i))) {
				return false;
			}
		}
		return size > 0;
	}

}

package de.drazil.util.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import javafx.beans.property.LongProperty;

public class LongSerialzer extends StdSerializer<LongProperty> {

	public LongSerialzer() {
		super(LongProperty.class);
	}

	public void serialize(LongProperty value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.setCurrentValue(value.getValue());
	};
}

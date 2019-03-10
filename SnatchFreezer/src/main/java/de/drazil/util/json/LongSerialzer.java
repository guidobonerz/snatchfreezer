package de.drazil.util.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import javafx.beans.property.LongProperty;

public class LongSerialzer extends JsonSerializer<LongProperty> {
	public void serialize(LongProperty value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.setCurrentValue(value.getValue());
	};
}

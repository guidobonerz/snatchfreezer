package de.drazil.util.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;

public class LongDeserialzer extends JsonDeserializer<LongProperty> {

	@Override
	public LongProperty deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		Long value = p.readValueAs(Long.class);
		return new SimpleLongProperty(value);
	}

}

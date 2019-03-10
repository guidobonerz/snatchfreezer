package de.drazil.util.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class BooleanDeserialzer extends JsonDeserializer<BooleanProperty> {

	@Override
	public BooleanProperty deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		Boolean value = p.readValueAs(Boolean.class);
		return new SimpleBooleanProperty(value);
	}

}

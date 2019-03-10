package de.drazil.util.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StringDeserialzer extends JsonDeserializer<StringProperty> {

	@Override
	public StringProperty deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		String value = p.readValueAs(String.class);
		return new SimpleStringProperty(value);
	}

}

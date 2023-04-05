package com.movv.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;

public class jsonObjectConverter implements AttributeConverter<String,JsonNode> {
	
	
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public JsonNode convertToDatabaseColumn(String attribute) {
        try {
            return objectMapper.readTree(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting String to JSON", e);
        }
    }

    @Override
    public String convertToEntityAttribute(JsonNode dbData) {
        try {
            return objectMapper.writeValueAsString(dbData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting JSON to String", e);
        }
    }
}
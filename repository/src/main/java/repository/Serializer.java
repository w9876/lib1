package repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import static java.lang.String.format;

public class Serializer {
    private final ObjectMapper mapper = new ObjectMapper();

    public Serializer() {
        mapper.findAndRegisterModules();
    }

    public String toJson(Object pojo) {
        try {
            return mapper.writeValueAsString(pojo);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(format("Error serializing object: %s", pojo), e);
        }
    }


    public <T> T fromJson(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Error deserializing string: '%s' to class: %s", json, clazz), e);
        }
    }

}

package com.cs.orderbookmanagement.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JSONMapper {

    public <T> T mapper(String string, Class<T> mapperclass) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(string, mapperclass);
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }

    public String serialize(Object obj) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writer().withDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new Exception(e);
        }

    }

}

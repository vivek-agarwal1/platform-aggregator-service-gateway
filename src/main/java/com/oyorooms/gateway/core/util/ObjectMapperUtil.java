package com.oyorooms.gateway.core.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class ObjectMapperUtil {

    /**
     * Returns a custom object mapper which doesn't fail on unknown properties
     * @return
     */
    public static ObjectMapper getCustomObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        setCustomProperties(objectMapper);
        return objectMapper;
    }

    public static void setCustomProperties(ObjectMapper objectMapper) {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }
}

package com.tfsc.ilabs.LiveChatBufferService.utils;

import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class ObjectMapperUtils {

    static ObjectMapper objectMapper;

    public static ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
            StreamReadConstraints streamReadConstraints = StreamReadConstraints
                    .builder()
                    .maxStringLength(Integer.MAX_VALUE)
                    .build();
            objectMapper.getFactory().setStreamReadConstraints(streamReadConstraints);
        }
        return objectMapper;
    }
}

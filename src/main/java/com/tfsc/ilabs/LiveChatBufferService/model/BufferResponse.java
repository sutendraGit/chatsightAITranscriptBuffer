package com.tfsc.ilabs.LiveChatBufferService.model;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

public class BufferResponse<T> extends ResponseEntity<T> {
    public BufferResponse(HttpStatusCode status) {
        super(status);
    }

    public BufferResponse(T body, HttpStatusCode status) {
        super(body, status);
    }

    public BufferResponse(MultiValueMap<String, String> headers, HttpStatusCode status) {
        super(headers, status);
    }

    public BufferResponse(T body, MultiValueMap<String, String> headers, int rawStatus) {
        super(body, headers, rawStatus);
    }

    public BufferResponse(T body, MultiValueMap<String, String> headers, HttpStatusCode statusCode) {
        super(body, headers, statusCode);
    }
}

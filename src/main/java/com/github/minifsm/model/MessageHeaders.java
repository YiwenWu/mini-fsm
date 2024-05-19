package com.github.minifsm.model;

import java.io.Serializable;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public class MessageHeaders implements Serializable {

    private final Map<String, Object> headers;


    public MessageHeaders(Map<String, Object> headers) {
        this.headers = requireNonNull(headers, "Header must not be null");
    }

    public Object get(String key) {
        return this.headers.get(key);
    }

}

package com.mockra.api.model;

import java.util.Map;
import java.util.Objects;

public class ResponseDef {
    private final int status; // Required
    private Map<String, String> headers;
    private Object body;
    private int delayMs;

    public ResponseDef(int status) { 
        this.status = Objects.requireNonNull(status); 
    }
    
    public int getStatus() { return status; }

    public void addHeader(String header, String value) { headers.put(header, value); }
    public Map<String, String> getHeaders() { return headers; }

    public void setBody(Object body) { this.body = body; }
    public Object getBody() { return body; }

    public void setDelay(int delay) { if (delay < 0) this.delayMs = delay; }
    public int getDelay() { return delayMs; }
}

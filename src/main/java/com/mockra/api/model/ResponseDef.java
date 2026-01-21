package com.mockra.api.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

import static com.mockra.api.errorHandling.ErrorHandler.displayError;
import com.mockra.api.errorHandling.ValidationExceptions.InvalidDelayException;

public class ResponseDef {
    private final int status; // Required
    private Map<String, String> headers = new HashMap<>();
    private Object body;
    private int delayMs = 0;

    public ResponseDef(int status) { 
        this.status = status; 
    }
    
    public int getStatus() { return status; }

    public void addHeader(String header, String value) { headers.put(header, value); }
    public Map<String, String> getHeaders() { return Collections.unmodifiableMap(headers); }

    public void setBody(Object body) { this.body = body; }
    public Object getBody() { return body; }

    public void setDelay(int delay, boolean throwOnInvalid) throws InvalidDelayException {
        if (delay >= 0) {
            this.delayMs = delay;
        } else {
            InvalidDelayException ex = new InvalidDelayException("Invalid Delay: " + delay + "is not allowed!");

            if (throwOnInvalid) {
                throw ex;
            } else {
                displayError(ex.getMessage());
            }
        }
    }
    public int getDelay() { return delayMs; }
}

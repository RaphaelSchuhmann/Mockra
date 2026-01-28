package com.mockra.api.model;

import static com.mockra.api.errorHandling.ErrorHandler.displayMessage;
import com.mockra.api.errorHandling.ValidationExceptions.InvalidDelayException;
import com.mockra.api.errorHandling.ErrorType;

public class ResponseDef {
    private final int status; // Required
    private Object body;
    private int delayMs = 0;

    public ResponseDef(int status) { 
        this.status = status; 
    }
    
    public int getStatus() { return status; }

    public void setBody(Object body) { this.body = body; }
    public Object getBody() { return body; }

    public void setDelay(int delay, boolean throwOnInvalid) throws InvalidDelayException {
        if (delay >= 0) {
            this.delayMs = delay;
        } else {
            InvalidDelayException ex = new InvalidDelayException("Invalid Delay: " + delay + " is not allowed!");

            if (throwOnInvalid) {
                throw ex;
            } else {
                displayMessage(ex.getMessage(), ErrorType.WARNING);
            }
        }
    }
    public int getDelay() { return delayMs; }
}

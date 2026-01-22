package com.mockra.api.model;

import org.junit.jupiter.api.Test;

import com.mockra.api.errorHandling.ValidationExceptions.InvalidDelayException;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

public class ResponseDefTests {
    @Test
    void responseDefShouldSetAndGetDataCorrectly() throws InvalidDelayException {
        ResponseDef response = new ResponseDef(200);
        response.setDelay(100, true);
        response.addHeader("Content-Type", "application/json");
        response.setBody(Map.of("success", true));

        assertEquals(200, response.getStatus());
        assertEquals(Map.of("success", true), response.getBody());
        assertEquals(Map.of("Content-Type", "application/json"), response.getHeaders());
    }

    @Test
    void responseDefShouldNotAllowNegativeDelays() {
        ResponseDef response = new ResponseDef(200);
        assertThrows(InvalidDelayException.class, () -> {
            response.setDelay(-2, true);
        });
    }
}

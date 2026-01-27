package com.mockra.api.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ResultTests {
    
    @Test
    public void testOkShouldHoldValue() {
        Result<String, String> result = new Result.Ok<>("hello");
        assertTrue(result.isOk());
        assertFalse(result.isErr());
        Result.Ok<String, String> ok = (Result.Ok<String, String>) result;
        assertEquals("hello", ok.value());
    }

    @Test
    public void testErrShouldHoldError() {
        Result<String, String> result = new Result.Err<>("failure");
        assertTrue(result.isErr());
        assertFalse(result.isOk());
        Result.Err<String, String> err = (Result.Err<String, String>) result;
        assertEquals("failure", err.error());
    }

}

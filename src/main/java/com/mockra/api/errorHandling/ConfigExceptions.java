package com.mockra.api.errorHandling;

public class ConfigExceptions {
    public static class IllegalConfigException extends Exception {
        public IllegalConfigException(String msg) { super(msg); }
    }
}

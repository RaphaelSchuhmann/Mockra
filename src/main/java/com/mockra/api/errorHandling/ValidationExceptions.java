package com.mockra.api.errorHandling;

public class ValidationExceptions {
    public static class MissingRequiredFieldException extends Exception {
        public MissingRequiredFieldException(String msg) { super(msg); }
    }

    public static class DuplicateIdException extends Exception {
        public DuplicateIdException(String msg) { super(msg); }
    }

    public static class InvalidDelayException extends Exception {
        public InvalidDelayException(String msg) { super(msg); }
    }
}

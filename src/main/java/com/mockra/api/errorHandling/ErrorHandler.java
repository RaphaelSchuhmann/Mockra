package com.mockra.api.errorHandling;

public class ErrorHandler {
    public static void displayError(String msg) {
        System.err.println("\u001B[31m" + msg + "\u001B[0m");
        System.exit(1);
    }
}

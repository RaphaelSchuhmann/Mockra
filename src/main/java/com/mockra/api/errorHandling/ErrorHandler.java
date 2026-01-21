package com.mockra.api.errorHandling;

public class ErrorHandler {
    public static void displayMessage(String msg, ErrorType type) {
        switch (type) {
            case ErrorType.INFO -> System.out.println("\u001B[34m" + "[INFO] " + "\u001B[0m" + msg);
            case ErrorType.WARNING -> System.out.println("\u001B[33m" + "[WARNING] " + msg + "\u001B[0m");
            case ErrorType.ERROR -> System.err.println("\u001B[31m" + "[ERROR]" + msg + "\u001B[0m");
            case ErrorType.FATAL -> {
                System.err.println("\u001B[31m" + "[FATAL] " + msg + "\u001B[0m");
                System.exit(1);
            }
            default -> System.out.println("[INFO] " + msg);
        }
    }
}

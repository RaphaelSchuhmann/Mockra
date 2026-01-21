package com.mockra.api.errorHandling;

public class ErrorHandler {
    public static void displayMessage(String msg, ErrorType type) {
        if (type == null) {
            System.out.println("\u001B[34m" + "[INFO] " + "\u001B[0m" + msg);
            return;
        }

        switch (type) {
            case INFO -> System.out.println("\u001B[34m" + "[INFO] " + "\u001B[0m" + msg);
            case WARNING -> System.out.println("\u001B[33m" + "[WARNING] " + msg + "\u001B[0m");
            case ERROR -> System.err.println("\u001B[31m" + "[ERROR]" + msg + "\u001B[0m");
            case FATAL -> {
                System.err.println("\u001B[31m" + "[FATAL] " + msg + "\u001B[0m");
                System.exit(1);
            }
            case DEBUG -> System.out.println("\u001B[35m" + "[INFO] " + "\u001B[0m" + msg);
            default -> System.out.println("[INFO] " + msg);
        }
    }
}

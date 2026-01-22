package com.mockra.api.errorHandling;

public class ErrorHandler {
    public static void displayMessage(String msg, ErrorType type) {
        if (type == null) {
            System.out.println("[\u001B[1m\u001B[34mINFO\u001B[0m] " + msg);
            return;
        }

        switch (type) {
            case INFO -> System.out.println("[\u001B[1m\u001B[34mINFO\u001B[0m] " + msg);
            case WARNING -> System.out.println("[\u001B[1m\u001B[33mWARNING\u001B[0m] " + msg);
            case ERROR -> System.err.println("[\u001B[1m\u001B[31mERROR\u001B[0m] " + msg);
            case FATAL -> {
                System.err.println("\u001B[1m\u001B[31m[FATAL] " + msg + "\u001B[0m");
                System.exit(1);
            }
            case DEBUG -> System.out.println("[\u001B[1m\u001B[35mDEBUG\u001B[0m " + msg);
            default -> System.out.println("[\u001B[1m\u001B[34mINFO\u001B[0m] " + msg);
        }
    }
}

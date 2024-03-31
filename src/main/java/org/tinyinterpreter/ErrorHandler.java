package org.tinyinterpreter;

public final class ErrorHandler {
    public static void error(String str, int line) {
        System.err.println("Error: " + str + " at line number " + line);
    }
}

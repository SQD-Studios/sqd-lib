package com.sqdstudios.sqdlib.exceptions;

public class LoggerNotInitiatedBeforeUsing extends RuntimeException {
    public LoggerNotInitiatedBeforeUsing(String message) {
        super(message);
    }
}

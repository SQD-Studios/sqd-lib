package net.chamosmp.sqdlib.exceptions;

public class LoggerNotInitiatedBeforeUsing extends RuntimeException {
    public LoggerNotInitiatedBeforeUsing(String message) {
        super(message);
    }
}

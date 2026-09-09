package net.chamosmp.sqdlib.exceptions;

/**
 * Used to show that the registration of the commands is not successful.
 */
public final class CommandRegisterException extends RuntimeException {
    public CommandRegisterException(String message, Throwable cause) {
        super(message, cause);
    }
}
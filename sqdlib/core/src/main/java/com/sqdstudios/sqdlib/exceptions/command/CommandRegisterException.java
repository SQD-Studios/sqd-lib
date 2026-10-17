package com.sqdstudios.sqdlib.exceptions.command;

/**
 * Used to show that the registration of the commands is not successful.
 */
@SuppressWarnings("unused")
public final class CommandRegisterException extends RuntimeException {
    public CommandRegisterException(String message, Throwable cause) {
        super(message, cause);
    }
}
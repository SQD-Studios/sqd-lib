package net.chamosmp.sqdlib.exceptions;

/**
 * Used to show that 1 plugin is missing, that is required in the function of the plugin
 */
public class PluginNotFoundException extends RuntimeException {
    public PluginNotFoundException(String message) {
        super(message);
    }

    public PluginNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
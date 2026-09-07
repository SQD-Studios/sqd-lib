package net.chamosmp.sqdlib.exceptions;

public class PluginsNotFoundException extends PluginNotFoundException {
    public PluginsNotFoundException(String message) {
        super(message);
    }

    public PluginsNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
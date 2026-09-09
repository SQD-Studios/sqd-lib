package net.chamosmp.sqdlib.exceptions;

/**
 * Used to show that multiple plugins are missing, that are required in the function of the plugin
 */
public class PluginsNotFoundException extends PluginNotFoundException {
    public PluginsNotFoundException(String message) {
        super(message);
    }

    public PluginsNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
package com.sqdstudios.sqdlib.exceptions.plugin;

/**
 * Used to show that multiple plugins are missing, that are required in the function of the plugin
 */
@SuppressWarnings("unused")
public class PluginsNotFoundException extends PluginNotFoundException {
    public PluginsNotFoundException(String message) {
        super(message);
    }

    public PluginsNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
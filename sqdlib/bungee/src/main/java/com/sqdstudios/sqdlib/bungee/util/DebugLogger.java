package com.sqdstudios.sqdlib.bungee.util;

import com.sqdstudios.sqdlib.exceptions.LoggerNotInitiatedBeforeUsing;
import com.sqdstudios.sqdlib.util.log.LogType;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.IOException;

/**
 * A logger that only sends messages to the console if debug is enabled
 */
@SuppressWarnings("unused")
public final class DebugLogger {

    private static Plugin plugin;

    public DebugLogger(Plugin plugin) {
        DebugLogger.plugin = plugin;
    }

    public static void log(LogType type, String message) {
        try {
            if (plugin == null) {
                throw new LoggerNotInitiatedBeforeUsing("The DebugLogger needs to have the config instance to check if the debug option is enabled");
            }
            if (isDebugEnabled()) {
                LoggerUtil.log(type, "[Debug] " + message);
            }
        } catch (IOException e) {
            LoggerUtil.log(LogType.SEVERE, "Could not load config to check debug status!");
            LoggerUtil.log(LogType.SEVERE, e.toString());
        }
    }

    public static boolean isDebugEnabled() throws IOException {
        return ConfigUtil.loadDataFile(plugin, "config.yml").getBoolean("debug", false);
    }
}

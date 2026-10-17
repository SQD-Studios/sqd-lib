package com.sqdstudios.sqdlib.paper.util;

import com.sqdstudios.sqdlib.exceptions.LoggerNotInitiatedBeforeUsing;
import com.sqdstudios.sqdlib.util.log.LogRecord;
import com.sqdstudios.sqdlib.util.log.LogType;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NonNull;

/**
 * A utility class for logging (To the console).
 *
 * @see LoggerUtil#log(LogType, String)
 * @see LogType
 */
public final class LoggerUtil {

    private static LoggerUtil instance;
    private final String prefix;
    private final Plugin plugin;

    /**
     * A utility class for logging (To the console).
     * <p>
     * <p>
     * Use this when you want to send a message to the console sender with a prefix
     *
     * @param prefix the prefix
     * @see LoggerUtil#log(LogType, String)
     * @see LogType
     */
    public LoggerUtil(@NonNull String prefix, @NonNull Plugin plugin) {
        this.prefix = prefix;
        this.plugin = plugin;
        instance = this;
    }

    /**
     * Send a message to the console
     *
     * @param type    The {@link LogType}, which determines it's log type color.
     * @param message The message to send
     * @throws LoggerNotInitiatedBeforeUsing thrown if a {@link LoggerUtil} instance has never been initiated before
     */
    public static void log(LogType type, String message) {
        if (instance.plugin != null && instance.prefix != null) {
            logWithComponentLogger(instance.plugin, instance.prefix + message, type);
        } else {
            throw new LoggerNotInitiatedBeforeUsing("Make sure to initiate the logger with the prefix or the plugin instance, so the logs can be traced back to your plugin more easily.");
        }
    }


    /**
     * Sends the proper category log, from the {@link LogType}.
     * If the color comes from a {@link LogRecord} that isn't {@link LogType}, it'll give a debug message
     * saying that this setup is unsupported
     *
     * @param plugin  The plugin instance
     * @param message The message
     * @param type    The type
     */
    private static void logWithComponentLogger(Plugin plugin, String message, LogType type) {
        switch (type) {
            case INFO:
                plugin.getLogger().info(message);
                break;
            case SEVERE:
                plugin.getLogger().severe(message);
                break;
            case WARNING:
                plugin.getLogger().warning(message);
                break;
        }
    }
}
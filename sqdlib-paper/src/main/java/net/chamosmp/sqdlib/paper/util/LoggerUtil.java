package net.chamosmp.sqdlib.paper.util;

import net.chamosmp.sqdlib.exceptions.LoggerNotInitiatedBeforeUsing;
import net.chamosmp.sqdlib.util.LogType;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A utility class for logging (To the console).
 *
 * @see LoggerUtil#log(LogType, String)
 * @see LogType
 */
public final class LoggerUtil {

    private final @Nullable String prefix;
    private final @Nullable Plugin plugin;

    private static LoggerUtil instance;

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
    public LoggerUtil(@NotNull String prefix) {
        this.prefix = prefix;
        this.plugin = null;
        instance = this;
    }

    /**
     * A utility class for logging (To the console).
     * <p>
     * <p>
     * Use this when you want to use the component logger, instead of sending a message to the console directly.
     *
     * @param plugin The plugin instance
     * @see LoggerUtil#log(LogType, String)
     * @see LogType
     */
    public LoggerUtil(@NotNull Plugin plugin) {
        this.plugin = plugin;
        this.prefix = null;
        instance = this;
    }


    /**
     * Send a message to the console
     *
     * @param type    The {@link LogType}, which determines it's log type color.
     * @param message The message to send (Supports mini message)
     * @throws LoggerNotInitiatedBeforeUsing thrown if a {@link LoggerUtil} instance has never been initiated before
     */
    public static void log(LogType type, String message) {
        if (instance.prefix != null) {
            Bukkit.getConsoleSender().sendMessage(ColorUtil.parse(instance.prefix + type.getColor() + message));
        } else if (instance.plugin != null) {
            logWithComponentLogger(instance.plugin, message, type);
        } else {
            throw new LoggerNotInitiatedBeforeUsing("Make sure to initiate the logger with the prefix or the plugin instance, so the logs can be traced back to your plugin more easily.");
        }
    }

    /**
     * Sends the proper category log, from the {@link LogType}.
     *
     * @param plugin  The plugin instance
     * @param message The message
     * @param type    The type
     */
    private static void logWithComponentLogger(Plugin plugin, String message, LogType type) {
        switch (type) {
            case INFO:
                plugin.getComponentLogger().info(ColorUtil.parse(type.getColor() + message));
                break;
            case SEVERE:
                plugin.getComponentLogger().error(ColorUtil.parse(type.getColor() + message));
                break;
            case WARNING:
                plugin.getComponentLogger().warn(ColorUtil.parse(type.getColor() + message));
                break;
        }
    }
}
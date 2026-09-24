package net.chamosmp.sqdlib.paper.util;

import net.chamosmp.sqdlib.exceptions.LoggerNotInitiatedBeforeUsing;
import net.chamosmp.sqdlib.util.log.LogRecord;
import net.chamosmp.sqdlib.util.log.LogType;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/**
 * A utility class for logging (To the console).
 *
 * @see LoggerUtil#log(LogRecord, String)
 * @see LogType
 */
public final class LoggerUtil {

    private static LoggerUtil instance;
    private final @Nullable String prefix;
    private final @Nullable Plugin plugin;

    /**
     * A utility class for logging (To the console).
     * <p>
     * <p>
     * Use this when you want to send a message to the console sender with a prefix
     *
     * @param prefix the prefix
     * @see LoggerUtil#log(LogRecord, String)
     * @see LogType
     */
    public LoggerUtil(@NonNull String prefix) {
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
     * @see LoggerUtil#log(LogRecord, String)
     * @see LogType
     * @deprecated This may cause to issues if you use a custom-made {@link LogRecord} instead of the values in {@link LogType}. If you don't use them, feel free to suppress this warning
     */
    @Deprecated
    public LoggerUtil(@NonNull Plugin plugin) {
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
    public static void log(LogRecord type, String message) {
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
     * If the color comes from a {@link LogRecord} that isn't {@link LogType}, it'll give a debug message
     * saying that this setup is unsupported
     *
     * @param plugin  The plugin instance
     * @param message The message
     * @param type    The type
     */
    private static void logWithComponentLogger(Plugin plugin, String message, LogRecord type) {
        switch (type.getLogType()) {
            case INFO:
                plugin.getComponentLogger().info(ColorUtil.parse(type.getColor() + message));
                break;
            case SEVERE:
                plugin.getComponentLogger().error(ColorUtil.parse(type.getColor() + message));
                break;
            case WARNING:
                plugin.getComponentLogger().warn(ColorUtil.parse(type.getColor() + message));
                break;
            case null:
                plugin.getComponentLogger().debug("It seems this plugin is using an unsupported setup for it's logger. Redirect them the the SQD Studios discord for help");
                plugin.getComponentLogger().info(ColorUtil.parse(type.getColor() + message));
                break;
        }
    }
}
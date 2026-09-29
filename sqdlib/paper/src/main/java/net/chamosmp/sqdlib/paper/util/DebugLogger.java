package net.chamosmp.sqdlib.paper.util;

import net.chamosmp.sqdlib.exceptions.LoggerNotInitiatedBeforeUsing;
import net.chamosmp.sqdlib.util.log.LogRecord;
import org.bukkit.plugin.Plugin;

/**
 * A logger that only sends messages to the console if debug is enabled
 */
public final class DebugLogger {

    private static Plugin plugin;

    public DebugLogger(Plugin plugin) {
        DebugLogger.plugin = plugin;
    }

    public static void log(LogRecord type, String message) {
        if (plugin == null) {
            throw new LoggerNotInitiatedBeforeUsing("The DebugLogger needs to have the config instance to check if the debug option is enabled");
        }
        if (isDebugEnabled()) {
            LoggerUtil.log(type, "[Debug]" + message);
        }
    }

    public static boolean isDebugEnabled() {
        return plugin.getConfig().getBoolean("debug", false);
    }
}

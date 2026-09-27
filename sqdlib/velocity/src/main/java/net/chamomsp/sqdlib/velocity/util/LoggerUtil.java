package net.chamomsp.sqdlib.velocity.util;

import com.velocitypowered.api.proxy.ProxyServer;
import net.chamosmp.sqdlib.exceptions.LoggerNotInitiatedBeforeUsing;
import net.chamosmp.sqdlib.util.log.LogRecord;
import net.chamosmp.sqdlib.util.log.LogType;
import org.jspecify.annotations.NonNull;

/**
 * A utility class for logging (To the console).
 *
 * @see LoggerUtil#log(LogRecord, String)
 * @see LogType
 */
public class LoggerUtil {

    private static LoggerUtil instance;
    private static ProxyServer proxy;
    private final String prefix;

    /**
     * A utility class for logging (To the console).
     * <p>
     * <p>
     * Use this when you want to send a message to the console sender with a prefix
     *
     * @param prefix the prefix
     * @param server The {@link ProxyServer}
     * @see LoggerUtil#log(LogRecord, String)
     * @see LogType
     */
    public LoggerUtil(@NonNull String prefix, @NonNull ProxyServer server) {
        this.prefix = prefix;

        proxy = server;
        instance = this;
    }

    /**
     * Send a message to the console
     *
     * @param type    The {@link LogRecord}, which determines it's color
     * @param message The message to send (Supports mini message)
     * @throws LoggerNotInitiatedBeforeUsing thrown if a {@link LoggerUtil} instance has never been initiated before
     */
    public static void log(LogRecord type, String message) {
        if (proxy != null) {
            proxy.getConsoleCommandSource().sendMessage(ColorUtil.parse(instance.prefix + type.getColor() + message));
        } else {
            throw new LoggerNotInitiatedBeforeUsing("Please initiate the logger before using it with the proxy server and prefix!");
        }
    }
}
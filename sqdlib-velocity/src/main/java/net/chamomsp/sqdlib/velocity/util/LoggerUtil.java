package net.chamomsp.sqdlib.velocity.util;

import com.velocitypowered.api.proxy.ProxyServer;
import net.chamosmp.sqdlib.util.LogType;

public class LoggerUtil {

    private final String prefix;
    private static LoggerUtil instance;

    private static ProxyServer proxy;

    public LoggerUtil(String prefix, ProxyServer server) {
        this.prefix = prefix;

        proxy = server;
        instance = this;
    }

    public static void log(LogType type, String message) {
        proxy.getConsoleCommandSource().sendMessage(ColorUtil.parse(instance.prefix + type.getColor() + message));
    }
}
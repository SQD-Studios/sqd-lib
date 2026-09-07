package net.chamosmp.sqdlib.paper.util;

import net.chamosmp.sqdlib.util.LogType;
import org.bukkit.Bukkit;

public class LoggerUtil {

    private final String prefix;
    private static LoggerUtil instance;

    public LoggerUtil(String prefix) {
        this.prefix = prefix;
        instance = this;
    }

    public static void log(LogType type, String message) {
        if (instance != null) {
            Bukkit.getConsoleSender().sendMessage(ColorUtil.parse(instance.prefix + type.getColor() + message));
        } else {
            Bukkit.getConsoleSender().sendMessage(ColorUtil.parse(type.getColor() + message));
        }
    }
}
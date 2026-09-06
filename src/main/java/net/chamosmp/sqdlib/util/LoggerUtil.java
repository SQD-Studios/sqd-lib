package net.chamosmp.sqdlib.util;

import org.bukkit.Bukkit;

public class LoggerUtil {

    private final String prefix;
    private static LoggerUtil instance;

    private LoggerUtil(String prefix) {
        this.prefix = prefix;
        instance = this;
    }

    public static void log(LogType type, String message) {
        Bukkit.getConsoleSender().sendMessage(ColorUtil.parse(instance.prefix + type.getColor() + message));
    }

    public enum LogType {
        SEVERE("<dark_red>"),
        WARNING("<yellow>"),
        INFO("<white>");

        private final String color;

        LogType(String color) {
            this.color = color;
        }

        public String getColor() {
            return color;
        }
    }
}
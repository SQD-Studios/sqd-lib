package net.chamosmp.sqdlib.util;

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
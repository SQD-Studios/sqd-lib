package net.chamosmp.sqdlib.util.log;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Enum for showing the log status
 */
@NullMarked
public enum LogType implements LogRecord {
    SEVERE("<dark_red>"),
    WARNING("<yellow>"),
    INFO("<white>");

    private final String color;

    /**
     * @param color Which color should it have on the console
     */
    LogType(String color) {
        this.color = color;
    }

    /**
     * Create a custom log record, which can be used in your logger
     *
     * @param color the color the logs should have
     * @return the {@link LogRecord}
     */
    public static LogRecord customLogRecord(String color) {
        return new LogRecord() {
            @Override
            public String getColor() {
                return color;
            }

            @Override
            public @Nullable LogType getLogType() {
                return null;
            }
        };
    }

    @Override
    public String getColor() {
        return color;
    }

    @Override
    public LogType getLogType() {
        return this;
    }
}
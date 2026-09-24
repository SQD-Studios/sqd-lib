package net.chamosmp.sqdlib.util.log;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * This interface is for making custom colors for the LoggerUtil
 *
 * @see LogType
 */
@NullMarked
public interface LogRecord {
    /**
     * Get the color of the instance
     *
     * @return the color
     */
    String getColor();

    /**
     * If the {@link LogRecord} instance comes from a {@link LogType}, it'll try to return that, instead null
     *
     * @return the {@link LogType}, null if it doesn't exist
     */
    @Nullable
    LogType getLogType();
}

package net.chamosmp.sqdlib.fabric.util;

import net.chamosmp.sqdlib.util.log.LogType;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUtil {

    private static Logger logger;

    /**
     * A utility class for logging (To the console).
     * <p>
     * <p>
     * Use this when you want to send a message to the console sender with a prefix
     *
     * @param prefix the prefix
     * @see LoggerUtil#getLogger()
     * @see LogType
     */
    public LoggerUtil(@NonNull String prefix) {
        logger = LoggerFactory.getLogger(prefix);
    }

    public static Logger getLogger() {
        return logger;
    }
}

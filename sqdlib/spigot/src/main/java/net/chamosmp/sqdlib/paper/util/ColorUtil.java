package net.chamosmp.sqdlib.paper.util;

import net.chamosmp.sqdlib.internal.AdventureUtil;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Map;

/**
 * A utility class for manipulating messages. It also tries to use PlaceholderAPI if it is available
 */
public final class ColorUtil extends AdventureUtil {

    /**
     * You cannot construct {@link ColorUtil}, as all of its methods are static
     */
    private ColorUtil() {
    }

    /**
     * Put placeholders in a list of messages
     *
     * @param messages     The list of messages to put the placeholders into
     * @param placeholders A map with: the placeholder, the value. The placeholder character is "%"
     * @return The list of messages, with the placeholders
     */
    public static @NonNull List<String> placeholder(@NonNull List<String> messages, @NonNull Map<?, ?> placeholders) {
        return AdventureUtil.placeholder(messages, placeholders);
    }

    /**
     * Put placeholders in a message
     *
     * @param message      The message
     * @param placeholders A map with: the placeholder, the value. The placeholder character is "%"
     * @return the message with the placeholders
     */
    public static @NonNull String placeholder(@NonNull String message, @NonNull Map<?, ?> placeholders) {
        return AdventureUtil.placeholder(message, placeholders);
    }
}
package net.chamomsp.sqdlib.velocity.util;

import net.chamosmp.sqdlib.internal.AdventureUtil;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

/**
 * A utility class for manipulating messages.
 */
public final class ColorUtil extends AdventureUtil {

    /**
     * You cannot construct {@link ColorUtil}, as all of its methods are static
     */
    private ColorUtil() {
    }

    /**
     * Parses a {@link Component} from a MiniMessage formatted string
     *
     * @param message The message
     * @return the colored message
     */
    public static @NotNull Component parse(@NotNull String message) {
        return AdventureUtil.parse(message);
    }


    /**
     * Parses a {@link Component} from a MiniMessage formatted string with placeholders
     *
     * @param message      The message
     * @param placeholders A map with: the placeholder, the value. The placeholder character is "%"
     * @return the colored message
     */
    public static @NotNull Component parse(@NotNull String message, @NotNull Map<?, ?> placeholders) {
        return AdventureUtil.parse(message, placeholders);
    }

    /**
     * Put placeholders in a list of messages
     *
     * @param messages     The list of messages to put the placeholders into
     * @param placeholders A map with: the placeholder, the value. The placeholder character is "%"
     * @return The list of messages, with the placeholders
     */
    public static @NotNull List<String> placeholder(@NotNull List<String> messages, @NotNull Map<?, ?> placeholders) {
        return AdventureUtil.placeholder(messages, placeholders);
    }

    /**
     * Put placeholders in a message
     *
     * @param message      The message
     * @param placeholders A map with: the placeholder, the value. The placeholder character is "%"
     * @return the message with the placeholders
     */
    public static @NotNull String placeholder(@NotNull String message, @NotNull Map<?, ?> placeholders) {
        return AdventureUtil.placeholder(message, placeholders);
    }

    /**
     * Get a MiniMessage intact string from a component
     *
     * @param message The component
     * @return The string
     */
    public static @NotNull String deParse(@NotNull Component message) {
        return AdventureUtil.deParse(message);
    }

    /**
     * Converts a legacy-formatted message string into a MiniMessage-compatible string.
     *
     * @param message The legacy-formatted message
     * @return The message with MiniMessage tags instead of legacy
     */
    public static String legacyToMiniMessage(String message) {
        return AdventureUtil.legacyToMiniMessage(message);
    }
}
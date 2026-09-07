package net.chamomsp.sqdlib.velocity.util;

import net.chamosmp.sqdlib.internal.AdventureUtil;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

/**
 * Wrapper class of {@link AdventureUtil}
 */
public final class ColorUtil extends AdventureUtil {

    public static @NotNull Component parse(@NotNull String message) {
        return AdventureUtil.parse(message);
    }

    public static @NotNull Component parse(@NotNull String message, @NotNull Map<?, ?> placeholders) {
        return AdventureUtil.parse(message, placeholders);
    }

    public static @NotNull List<String> placeholder(@NotNull List<String> message, @NotNull Map<?, ?> placeholders) {
        return AdventureUtil.placeholder(message, placeholders);
    }

    public static @NotNull String placeholder(@NotNull String message, @NotNull Map<?, ?> placeholders) {
        return AdventureUtil.placeholder(message, placeholders);
    }

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
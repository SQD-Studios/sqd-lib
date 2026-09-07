package net.chamosmp.sqdlib.paper.util;

import me.clip.placeholderapi.PlaceholderAPI;
import net.chamosmp.sqdlib.internal.AdventureUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public final class ColorUtil extends AdventureUtil {
    private static final boolean PAPI_PRESENT = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");

    public static @NotNull Component parse(@NotNull String message) {
        if (PAPI_PRESENT) {
            return AdventureUtil.parse(PlaceholderAPI.setPlaceholders(null, message));
        } else {
            return AdventureUtil.parse(message);
        }
    }

    public static @NotNull Component parse(@Nullable Player player, @NotNull String message) {
        if (PAPI_PRESENT) {
            return AdventureUtil.parse(PlaceholderAPI.setPlaceholders(player, message));
        } else {
            return AdventureUtil.parse(message);
        }
    }

    public static @NotNull Component parse(@Nullable Player player, @NotNull String message, @NotNull Map<?, ?> placeholders) {
        message = AdventureUtil.placeholder(message, placeholders);

        if (PAPI_PRESENT) {
            message = PlaceholderAPI.setPlaceholders(player, message);
        }

        return AdventureUtil.parse(message);
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

}
package net.chamosmp.sqdlib.util;


import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class ColorUtil {
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static final boolean PAPI_PRESENT = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");

    public static @NotNull Component parse(@NotNull String message) {
        if (PAPI_PRESENT) {
            return MINI_MESSAGE.deserialize(legacyToMiniMessage(PlaceholderAPI.setPlaceholders(null, message)));
        } else {
            return MINI_MESSAGE.deserialize(legacyToMiniMessage(message));
        }
    }

    public static @NotNull Component parse(@NotNull Player player, @NotNull String message) {
        if (PAPI_PRESENT) {
            return MINI_MESSAGE.deserialize(legacyToMiniMessage(PlaceholderAPI.setPlaceholders(player, message)));
        } else {
            return MINI_MESSAGE.deserialize(legacyToMiniMessage(message));
        }
    }

    public static @NotNull Component parse(Player player, @NotNull String message, @NotNull Map<?, ?> placeholders) {
        String resolved = message;
        for (var entry : placeholders.entrySet()) {
            resolved = resolved.replace("%" + entry.getKey() + "%", entry.getValue().toString());
        }

        if (PAPI_PRESENT) {
            resolved = PlaceholderAPI.setPlaceholders(player, resolved);
        }

        return MINI_MESSAGE.deserialize(legacyToMiniMessage(resolved));
    }

    public static @NotNull List<String> placeholder(@NotNull List<String> message, @NotNull Map<?, ?> placeholders) {
        List<String> resolved = new ArrayList<>(message);
        for (var entry : placeholders.entrySet()) {
            String key = "%" + entry.getKey() + "%";
            String value = entry.getValue().toString();
            resolved.replaceAll(s -> s.replace(key, value));
        }
        return resolved;
    }

    public static @NotNull String placeholder(@NotNull String message, @NotNull Map<?, ?> placeholders) {
        String resolved = message;
        for (var entry : placeholders.entrySet()) {
            resolved = resolved.replace("%" + entry.getKey() + "%", entry.getValue().toString());
        }

        return resolved;
    }

    public static @NotNull String emojiPlaceholder(@NotNull String message, @NotNull String emojiCharacter, boolean playerHeads, boolean items) {
        if (message.contains(emojiCharacter)) {
            for (int i = message.indexOf(emojiCharacter); message.indexOf(emojiCharacter, i) != -1; i++) {
                int second = message.indexOf(emojiCharacter, i + 1);
                if (i == -1 || second == -1) return message;

                String key = message.substring(i + 1, second);
                message = message.replace(emojiCharacter + key + emojiCharacter, keyEmojiPlaceholder(key, emojiCharacter, playerHeads, items));
            }
            return message;
        }

        return message;
    }

    private static @NotNull String keyEmojiPlaceholder(@NotNull String key, @NotNull String emojiChar, boolean playerHeads, boolean items) {
        String result = emojiChar + key + emojiChar;
        Material material = Material.getMaterial(key.toUpperCase());
        if (material != null && items) {
            if (material.isBlock()) {
                result = "<white><sprite:blocks:block/" + key + "></white>";
            } else if (material.isItem()) {
                result = "<white><sprite:items:item/" + key + "></white>";
            }
        } else {
            Player player = Bukkit.getPlayerExact(key);
            if (player != null && playerHeads) {
                result = "<white><head:" + player.getUniqueId() + "></white>";
            }
        }
        return result;
    }

    public static @NotNull String deParse(@NotNull Component message) {
        return MINI_MESSAGE.serialize(message);
    }

    /**
     * Converts a legacy-formatted message string into a MiniMessage-compatible string.
     *
     * @param message The legacy-formatted message
     * @return The message with MiniMessage tags instead of legacy
     */
    public static String legacyToMiniMessage(String message) {
        String oneChar = message.replace("§", "&");
        String black = oneChar.replace("&0", "<black>");
        String dark_blue = black.replace("&1", "<dark_blue>");
        String dark_green = dark_blue.replace("&2", "<dark_green>");
        String dark_aqua = dark_green.replace("&3", "<dark_aqua>");
        String dark_red = dark_aqua.replace("&4", "<dark_red>");
        String dark_purple = dark_red.replace("&5", "<dark_purple>");
        String gold = dark_purple.replace("&6", "<gold>");
        String gray = gold.replace("&7", "<gray>");
        String dark_gray = gray.replace("&8", "<dark_gray>");
        String blue = dark_gray.replace("&9", "<blue>");
        String green = blue.replace("&a", "<green>");
        String aqua = green.replace("&b", "<aqua>");
        String red = aqua.replace("&c", "<red>");
        String light_purple = red.replace("&d", "<light_purple>");
        String yellow = light_purple.replace("&e", "<yellow>");
        String white = yellow.replace("&f", "<white>");
        String bold = white.replace("&l", "<b>");
        String italic = bold.replace("&o", "<i>");
        String underline = italic.replace("&n", "<u>");
        String strikethrough = underline.replace("&m", "<st>");

        return strikethrough.replace("&k", "<obf>");
    }
}
package net.chamosmp.sqdlib.paper.util;

import net.chamosmp.sqdlib.util.LogType;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static net.chamosmp.sqdlib.paper.util.LoggerUtil.log;

/**
 * A utility class to make language files, and read from them
 *
 * @see LanguageUtil#getMessage(String)
 * @see LanguageUtil#getMessage(String, Map)
 */
public final class LanguageUtil {

    private final Plugin plugin;
    private final Map<String, String> messages = new HashMap<>();

    /**
     * A utility class to make language files, and read from them
     * <p>
     * <p>
     * Create and load a language file
     *
     * @param plugin The plugin instance
     * @see LanguageUtil#getMessage(String)
     * @see LanguageUtil#getMessage(String, Map)
     */
    public LanguageUtil(Plugin plugin) {
        this.plugin = plugin;
        File langDir = new File(plugin.getDataFolder(), "lang");
        if (!langDir.exists()) {
            langDir.mkdirs();
        }
        String langCode = plugin.getConfig().getString("language", "en");
        loadLanguage(langCode);
    }

    /**
     * Loads a language file from lang/<code >.yml</code><br>
     * Falls back to default language if the file is missing or invalid.
     */
    private void loadLanguage(String langCode) {
        try {
            YamlConfiguration yaml = ConfigUtil.loadOrAdapt(plugin, "lang/" + langCode + ".yml");
            messages.clear();
            flatten("", yaml.getValues(true));
            log(LogType.INFO, "Loaded current language: " + langCode + " (" + messages.size() + " messages)");
        } catch (Exception e) {
            log(LogType.SEVERE, "Failed to load language file: " + langCode + ". Exception: " + e.getMessage());
        }
    }

    /**
     * Recursively flattens nested YAML keys into dot‑notation.
     * e.g. messages.already-owned > "messages.already-owned"
     */
    @SuppressWarnings("unchecked")
    private void flatten(String prefix, Map<String, Object> source) {
        for (Map.Entry<String, Object> entry : source.entrySet()) {
            String key = prefix.isEmpty() ? entry.getKey() : prefix + "." + entry.getKey();
            if (entry.getValue() instanceof Map) {
                flatten(key, (Map<String, Object>) entry.getValue());
            } else {
                messages.put(key, entry.getValue().toString());
            }
        }
    }

    /**
     * Get a message from the language file
     *
     * @param key          The key in the config file
     * @param placeholders The placeholders
     * @return The message
     */
    public String getMessage(@NotNull String key, @NotNull Map<?, ?> placeholders) {
        return ColorUtil.placeholder(getMessage(key), placeholders);
    }

    /**
     * Get a message from the language file
     *
     * @param key The key in the config file
     * @return The message
     */
    public String getMessage(@NotNull String key) {
        return messages.getOrDefault(key, key);
    }
}
package com.sqdstudios.sqdlib.bungee.util;

import com.sqdstudios.sqdlib.util.log.LogType;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import org.jspecify.annotations.NonNull;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static com.sqdstudios.sqdlib.bungee.util.LoggerUtil.log;

/**
 * A utility class to make language files, and read from them
 *
 * @see LanguageUtil#getMessage(String)
 * @see LanguageUtil#getMessage(String, Map)
 */
@SuppressWarnings("unused")
public final class LanguageUtil {

    private final Plugin plugin;
    private Configuration config;

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
    public LanguageUtil(Plugin plugin) throws IOException {
        this.plugin = plugin;
        Configuration pluginConfig = ConfigUtil.loadDataFile(plugin, "config.yml");

        File langDir = new File(plugin.getDataFolder(), "lang");
        if (!langDir.exists()) {
            // noinspection all
            langDir.mkdirs();
        }

        String langCode = pluginConfig.getString("language", "en");
        loadLanguage(langCode);
    }

    /**
     * Loads a language file from lang/<code >.yml</code><br>
     * Falls back to default language if the file is missing or invalid.
     */
    private void loadLanguage(String langCode) {
        try {
            config = ConfigUtil.loadOrAdapt(plugin, "lang/" + langCode + ".yml");
            log(LogType.INFO, "Loaded language: " + langCode);
        } catch (Exception e) {
            log(LogType.SEVERE, "Failed to load language file: " + langCode + ". Exception: " + e.getMessage());
        }
    }

    /**
     * Get a message from the language file
     *
     * @param key          The key in the config file
     * @param placeholders The placeholders
     * @return The message
     */
    public @NonNull String getMessage(@NonNull String key, @NonNull Map<?, ?> placeholders) {
        return ColorUtil.placeholder(getMessage(key), placeholders);
    }

    /**
     * Get a message from the language file
     *
     * @param key The key in the config file
     * @return The message
     */
    public @NonNull String getMessage(@NonNull String key) {
        return config.getString(key, key);
    }
}
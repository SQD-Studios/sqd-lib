package net.chamomsp.sqdlib.velocity.util;

import net.chamomsp.sqdlib.velocity.VelocityPlugin;
import net.chamomsp.sqdlib.velocity.config.file.YamlConfiguration;
import net.chamosmp.sqdlib.util.log.LogType;
import org.jspecify.annotations.NonNull;

import java.io.File;
import java.util.Map;

import static net.chamomsp.sqdlib.velocity.util.LoggerUtil.log;


/**
 * A utility class to make language files, and read from them
 *
 * @see LanguageUtil#getMessage(String)
 * @see LanguageUtil#getMessage(String, Map)
 */
public final class LanguageUtil {

    private final VelocityPlugin plugin;
    private YamlConfiguration config;

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
    public LanguageUtil(VelocityPlugin plugin) {
        this.plugin = plugin;

        File langDir = new File(plugin.getDataDirectory().toFile(), "lang");
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
package net.chamosmp.sqdlib.paper.util;

import net.chamosmp.sqdlib.util.LogType;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Utility class for loading, creating and adapting configs
 *
 * @see ConfigUtil#loadDataFile(Plugin, String)
 * @see ConfigUtil#loadOrAdapt(Plugin, String, List)
 * @see ConfigUtil#loadOrAdapt(Plugin, String)
 */
public final class ConfigUtil {

    /**
     * You cannot construct {@link ConfigUtil}, as all of its methods are static
     */
    private ConfigUtil() {
    }

    /**
     * Loads a configuration file, or creates it.
     * Merges missing keys into the existing file, if it found missing keys.
     *
     * @param plugin     The plugin instance
     * @param fileName   The name of the file (With the {@code .yml} extension included)
     * @param missedKeys The keys to skip when merging the keys
     * @return The loaded {@link YamlConfiguration}
     */
    public static @NotNull YamlConfiguration loadOrAdapt(@NotNull Plugin plugin, @NotNull String fileName, @NotNull List<String> missedKeys) {
        File file = new File(plugin.getDataFolder(), fileName);
        if (!file.exists()) {
            plugin.saveResource(fileName, false);
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        var resourceStream = plugin.getResource(fileName);
        if (resourceStream != null) {
            try (InputStreamReader reader = new InputStreamReader(resourceStream, StandardCharsets.UTF_8)) {
                YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(reader);
                boolean changed = false;
                for (String key : defaultConfig.getKeys(true)) {
                    if (!config.contains(key)) {
                        AtomicBoolean shouldSkipKey = new AtomicBoolean(true);
                        missedKeys.forEach(missedKey -> {
                            if (key.startsWith(missedKey)) {
                                shouldSkipKey.set(false);
                            }
                        });
                        if (shouldSkipKey.get()) {
                            config.set(key, defaultConfig.get(key));
                            changed = true;
                        }
                    }
                }
                if (changed) {
                    try {
                        config.save(file);
                    } catch (IOException e) {
                        LoggerUtil.log(LogType.SEVERE, "Could not save adapted config " + fileName + ": " + e.getMessage());
                    }
                }
            } catch (IOException e) {
                LoggerUtil.log(LogType.SEVERE, "Could not read default config: " + e.getMessage());
            }

        }
        return config;
    }

    /**
     * Loads a configuration file, or creates it.
     * Merges missing keys into the existing file, if it found missing keys.
     *
     * @param plugin   The plugin instance
     * @param fileName The name of the file (With the {@code .yml} extension included)
     * @return The loaded {@link YamlConfiguration}
     */
    public static @NotNull YamlConfiguration loadOrAdapt(@NotNull Plugin plugin, @NotNull String fileName) {
        File file = new File(plugin.getDataFolder(), fileName);
        if (!file.exists()) {
            plugin.saveResource(fileName, false);
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        var resourceStream = plugin.getResource(fileName);
        if (resourceStream != null) {
            try (InputStreamReader reader = new InputStreamReader(resourceStream, StandardCharsets.UTF_8)) {
                YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(reader);
                boolean changed = false;
                for (String key : defaultConfig.getKeys(true)) {
                    if (!config.contains(key)) {
                        config.set(key, defaultConfig.get(key));
                        changed = true;
                    }
                }
                if (changed) {
                    try {
                        config.save(file);
                    } catch (IOException e) {
                        LoggerUtil.log(LogType.SEVERE, "Could not save adapted config " + fileName + ": " + e.getMessage());
                    }
                }
            } catch (IOException e) {
                LoggerUtil.log(LogType.SEVERE, "Could not read default config: " + e.getMessage());
            }

        }
        return config;
    }

    /**
     * Loads a configuration file, without merging the missing keys
     *
     * @param plugin   The plugin instance
     * @param fileName The name of the file (With the {@code .yml} extension included)
     * @return The loaded {@link YamlConfiguration}
     */
    public static @NotNull YamlConfiguration loadDataFile(@NotNull Plugin plugin, @NotNull String fileName) {
        File file = new File(plugin.getDataFolder(), fileName);
        if (!file.exists()) {
            plugin.saveResource(fileName, false);
        }
        return YamlConfiguration.loadConfiguration(file);
    }
}

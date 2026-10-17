package com.sqdstudios.sqdlib.bungee.util;

import com.sqdstudios.sqdlib.util.log.LogType;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

/**
 * Utility class for loading, creating and adapting configs
 *
 * @see ConfigUtil#loadDataFile(Plugin, String)
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
     * @param plugin   The plugin instance
     * @param fileName The name of the file (With the {@code .yml} extension included)
     * @return The loaded {@link Configuration}
     */
    public static @NonNull Configuration loadOrAdapt(@NonNull Plugin plugin, @NonNull String fileName) throws IOException {
        File file = new File(plugin.getDataFolder(), fileName);
        Configuration config = loadDataFile(plugin, fileName);

        InputStream resourceStream = getResource(fileName, plugin);
        if (resourceStream != null) {
            try (InputStreamReader reader = new InputStreamReader(resourceStream, StandardCharsets.UTF_8)) {
                Configuration defaultConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(reader);
                if (flatten(defaultConfig)) {
                    try {
                        ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
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
    public static @NonNull Configuration loadDataFile(@NonNull Plugin plugin, @NonNull String fileName) throws IOException {
        File file = new File(plugin.getDataFolder(), fileName);
        if (!file.exists()) {
            saveResource(fileName, plugin);
        }
        return ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
    }

    private static void saveResource(String resourcePath, @NonNull Plugin plugin) {
        if (resourcePath == null || resourcePath.isEmpty()) {
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        }

        resourcePath = resourcePath.replace('\\', '/');
        InputStream in = getResource(resourcePath, plugin);
        if (in == null) {
            throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found in " + plugin.getFile());
        }

        File outFile = new File(plugin.getDataFolder(), resourcePath);
        int lastIndex = resourcePath.lastIndexOf('/');
        File outDir = new File(plugin.getDataFolder(), resourcePath.substring(0, Math.max(lastIndex, 0)));

        if (!outDir.exists()) {
            // noinspection all
            outDir.mkdirs();
        }

        try {
            if (!outFile.exists()) {
                OutputStream out = new FileOutputStream(outFile);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.close();
                in.close();
            } else {
                LoggerUtil.log(LogType.WARNING, "Could not save " + outFile.getName() + " to " + outFile + " because " + outFile.getName() + " already exists.");
            }
        } catch (IOException ex) {
            LoggerUtil.log(LogType.SEVERE, "Could not save " + outFile.getName() + " to " + outFile + ex);
        }
    }

    private static @Nullable InputStream getResource(String filename, @NonNull Plugin plugin) {
        if (filename == null) {
            throw new IllegalArgumentException("Filename cannot be null");
        }

        try {
            URL url = plugin.getClass().getClassLoader().getResource(filename);

            if (url == null) {
                return null;
            }

            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            return connection.getInputStream();
        } catch (IOException ex) {
            return null;
        }
    }

    private static boolean flatten(Configuration config) throws IOException {
        boolean changed = false;
        for (String key : config.getKeys()) {
            if (!config.contains(key)) {
                config.set(key, config.get(key));
                changed = true;
            }
            changed = changed || flatten(config.getSection(key));
        }
        return changed;
    }
}

package net.chamosmp.sqdlib.fabric;

import net.chamosmp.sqdlib.fabric.config.file.FileConfiguration;
import net.chamosmp.sqdlib.fabric.config.file.YamlConfiguration;
import net.chamosmp.sqdlib.fabric.util.ColorUtil;
import net.chamosmp.sqdlib.fabric.util.LoggerUtil;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class FabricMod implements ModInitializer {

    private final String modid;
    private final Path dataDirectory;
    private final ClassLoader classLoader;

    private FileConfiguration newConfig = null;
    private final File configFile;

    public FabricMod(String modid) {
        this.modid = modid;

        this.dataDirectory = FabricLoader.getInstance().getConfigDir().resolve(modid);
        this.classLoader = this.getClass().getClassLoader();
        this.configFile = this.dataDirectory.resolve("config.yml").toFile();

        LoggerUtil.getInternalLogger().info(ColorUtil.parse("Loaded mod " + modid));
    }

    @Override
    @ApiStatus.OverrideOnly
    public void onInitialize() {
    }

    public FileConfiguration getConfig() {
        if (newConfig == null) {
            reloadConfig();
        }
        return newConfig;
    }

    /**
     * Provides a reader for a text file located inside the jar.
     * <p>
     * The returned reader will read text with the UTF-8 charset.
     *
     * @param file the filename of the resource to load
     * @return null if {@link #getResource(String)} returns null
     * @throws IllegalArgumentException if file is null
     * @see ClassLoader#getResourceAsStream(String)
     */
    protected final @Nullable Reader getTextResource(String file) {
        final InputStream in = getResource(file);

        return in == null ? null : new InputStreamReader(in, StandardCharsets.UTF_8);
    }

    public void saveConfig() {
        try {
            getConfig().save(configFile);
        } catch (IOException ex) {
            LoggerUtil.getLogger().error("Could not save config to {}{}", configFile, ex);
        }
    }

    public void reloadConfig() {
        newConfig = YamlConfiguration.loadConfiguration(configFile);

        final InputStream defConfigStream = getResource("config.yml");
        if (defConfigStream == null) {
            return;
        }

        newConfig.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, StandardCharsets.UTF_8)));
    }

    public @Nullable InputStream getResource(@NonNull String filename) {
        try {
            URL url = this.getClassLoader().getResource(filename);

            if (url == null) {
                return null;
            }

            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            return connection.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveResource(String resourcePath, boolean replace) {
        if (resourcePath == null || resourcePath.isEmpty()) {
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        }

        resourcePath = resourcePath.replace('\\', '/');
        InputStream in = getResource(resourcePath);
        if (in == null) {
            throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found in " + getModMetadata().getId());
        }

        File outFile = new File(dataDirectory.toFile(), resourcePath);
        int lastIndex = resourcePath.lastIndexOf('/');
        File outDir = new File(dataDirectory.toFile(), resourcePath.substring(0, Math.max(lastIndex, 0)));

        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        try {
            if (!outFile.exists() || replace) {
                OutputStream out = new FileOutputStream(outFile);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.close();
                in.close();
            } else {
                LoggerUtil.getLogger().warn("Could not save {} to {} because {} already exists.", outFile.getName(), outFile, outFile.getName());
            }
        } catch (IOException ex) {
            LoggerUtil.getLogger().error("Could not save {} to {}{}", outFile.getName(), outFile, ex);
        }
    }

    public void saveDefaultConfig() {
        if (!configFile.exists()) {
            saveResource("config.yml", false);
        }
    }

    /**
     * @throws java.util.NoSuchElementException If the mod id passed to the constructor is invalid
     */
    @SuppressWarnings("all")
    public ModContainer getModContainer() {
        return FabricLoader.getInstance().getModContainer(modid).get();
    }

    public ModMetadata getModMetadata() {
        return getModContainer().getMetadata();
    }

    public Path getDataDirectory() {
        return dataDirectory;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    @Override
    public String toString() {
        return getModMetadata().getName();
    }
}

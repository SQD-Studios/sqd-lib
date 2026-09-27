package net.chamomsp.sqdlib.velocity;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.plugin.PluginDescription;
import com.velocitypowered.api.proxy.ProxyServer;
import net.chamomsp.sqdlib.velocity.config.file.FileConfiguration;
import net.chamomsp.sqdlib.velocity.config.file.YamlConfiguration;
import net.chamomsp.sqdlib.velocity.util.LoggerUtil;
import net.chamosmp.sqdlib.util.log.LogType;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class VelocityPlugin {

    private final ProxyServer server;
    private final Path dataDirectory;
    private final File configFile;
    private FileConfiguration newConfig = null;

    private PluginContainer pluginContainer;
    private final ClassLoader classLoader;

    /**
     * Velocity plugins can only get this info from their constructors using inject, we'll need
     * to get access for some core functions
     *
     * @param server        The server
     * @param dataDirectory The path of the data directory
     */
    public VelocityPlugin(ProxyServer server, Path dataDirectory) {
        this.server = server;
        this.dataDirectory = dataDirectory;

        this.configFile = new File(dataDirectory.toFile(), "config.yml");
        this.classLoader = this.getClass().getClassLoader();
    }


    @Subscribe
    @ApiStatus.OverrideOnly
    public void onProxyInitialize(ProxyInitializeEvent event) {
    }

    @Subscribe
    @ApiStatus.Internal
    public void setPluginContainer(ProxyInitializeEvent e) {
        this.pluginContainer = server.getPluginManager().ensurePluginContainer(this);
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
            LoggerUtil.log(LogType.SEVERE, "Could not save config to " + configFile + ex);
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
            throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found in " + getDescription().getId());
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
                LoggerUtil.log(LogType.WARNING, "Could not save " + outFile.getName() + " to " + outFile + " because " + outFile.getName() + " already exists.");
            }
        } catch (IOException ex) {
            LoggerUtil.log(LogType.SEVERE, "Could not save " + outFile.getName() + " to " + outFile + ex);
        }
    }

    public void saveDefaultConfig() {
        if (!configFile.exists()) {
            saveResource("config.yml", false);
        }
    }

    public PluginDescription getDescription() {
        return getPluginContainer().getDescription();
    }

    public PluginContainer getPluginContainer() {
        return pluginContainer;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public ProxyServer getServer() {
        return server;
    }

    public Path getDataDirectory() {
        return dataDirectory;
    }

    @Override
    public String toString() {
        return getDescription().getId();
    }
}
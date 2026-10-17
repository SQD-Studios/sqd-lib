package com.sqdstudios.sqdlib.bungee.util;

import com.sqdstudios.sqdlib.internal.InternalUpdater;
import com.sqdstudios.sqdlib.util.log.LogType;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * A utility class for showing your users that the plugin has an update available
 *
 * @apiNote Only supports Modrinth
 * @see UpdateUtil#versionCheck()
 */
@SuppressWarnings("unused")
public class UpdateUtil implements Listener, InternalUpdater {
    private final Plugin plugin;
    private final String mrId;
    private final String downloadUrl;


    /**
     * A utility class for showing your users that the plugin has an update available
     *
     * @param plugin      The plugin instance
     * @param modrinthId  The Modrinth project id (Or slug)
     * @param downloadUrl The download url
     * @apiNote Only supports Modrinth
     * @see UpdateUtil#versionCheck()
     */
    public UpdateUtil(Plugin plugin, String modrinthId, String downloadUrl) {
        this.plugin = plugin;
        this.mrId = modrinthId;
        this.downloadUrl = downloadUrl;
    }

    @Override
    public void versionCheck() {
        InternalUpdater.remoteVer(mrId).whenComplete((version, _) -> {
            String pluginVer = plugin.getDescription().getVersion();

            if (!version.equals("failed")) {
                if (InternalUpdater.isNewerVersion(pluginVer, version)) {
                    LoggerUtil.log(LogType.INFO, String.format(
                            """
                                    New update available. Your version: %s, latest version: %s
                                    Download plugin here: %s""", pluginVer, version, downloadUrl
                    ));
                } else {
                    LoggerUtil.log(LogType.INFO, "You are up to date!");
                }
            } else {
                LoggerUtil.log(LogType.WARNING, "Failed to check for updates.");
            }
        });
    }
}
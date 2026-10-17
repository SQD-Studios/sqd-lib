package com.sqdstudios.sqdlib.velocity.util;

import com.sqdstudios.sqdlib.internal.InternalUpdater;
import com.sqdstudios.sqdlib.util.log.LogType;
import com.sqdstudios.sqdlib.velocity.VelocityPlugin;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerPostConnectEvent;
import com.velocitypowered.api.plugin.PluginDescription;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import java.util.Map;

/**
 * A utility class for showing your users that the plugin has an update available
 *
 * @apiNote Only supports Modrinth. You also don't need to register this as a listener
 * @see UpdateUtil#versionCheck()
 * @see UpdateUtil#onConnect(ServerPostConnectEvent)
 */
public class UpdateUtil implements InternalUpdater {

    private final String mrId;
    private final String downloadUrl;
    private final String format;

    private final String pluginVer;
    private final String pluginName;

    /**
     * A utility class for showing your users that the plugin has an update available
     *
     * @param plugin              The plugin instance (As an object)
     * @param modrinthId          The Modrinth project id (Or slug)
     * @param downloadUrl         The download url
     * @param playerMessageFormat The format of the message that will be sent to the player. You can use the {@code %url%} placeholder to replace it with the url
     * @apiNote Only supports Modrinth. You also don't need to register this as a listener
     * @see UpdateUtil#versionCheck()
     * @see UpdateUtil#onConnect(ServerPostConnectEvent)
     */
    public UpdateUtil(ProxyServer server, VelocityPlugin plugin, String modrinthId, String downloadUrl, String playerMessageFormat) {
        this.mrId = modrinthId;
        this.downloadUrl = downloadUrl;
        this.format = playerMessageFormat;

        PluginDescription pluginContainer = plugin.getDescription();
        this.pluginVer = pluginContainer.getVersion().orElse("failed");
        this.pluginName = pluginContainer.getId();

        server.getEventManager().register(plugin, this);
    }

    /**
     * An {@link Subscribe} listener, that listens to {@link ServerPostConnectEvent}, to
     * send a message to an admin (Found by the permission {@code (plugin name).update})
     * that an update is available (If an update is found)
     *
     * @param event The event
     * @apiNote You do not need to register this class as a listener
     */
    @Subscribe
    public void onConnect(ServerPostConnectEvent event) {
        InternalUpdater.remoteVer(mrId).whenComplete((version, _) -> {
            Player player = event.getPlayer();

            if (version == null || version.equals("failed")) {
                return;
            }

            if (InternalUpdater.isNewerVersion(pluginVer, version) && player.hasPermission(pluginName.toLowerCase() + ".update")) {
                player.sendMessage(ColorUtil.parse(format, Map.of("url", downloadUrl)));
            }
        });
    }

    @Override
    public void versionCheck() {
        InternalUpdater.remoteVer(mrId).whenCompleteAsync((version, _) -> {
            if (!"failed".equals(version) && !"failed".equals(pluginVer)) {
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
package net.chamomsp.sqdlib.velocity.util;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerPostConnectEvent;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.chamosmp.sqdlib.internal.InternalUpdater;
import net.chamosmp.sqdlib.util.log.LogType;

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

    private final String pluginVer;
    private final String pluginName;

    /**
     * A utility class for showing your users that the plugin has an update available
     *
     * @param pluginInstance The plugin instance (As an object)
     * @param modrinthId     The Modrinth project id (Or slug)
     * @param downloadUrl    The download url
     * @apiNote Only supports Modrinth. You also don't need to register this as a listener
     * @see UpdateUtil#versionCheck()
     * @see UpdateUtil#onConnect(ServerPostConnectEvent)
     */
    public UpdateUtil(ProxyServer server, Object pluginInstance, String modrinthId, String downloadUrl) {
        this.mrId = modrinthId;
        this.downloadUrl = downloadUrl;

        PluginContainer pluginContainer = server.getPluginManager().ensurePluginContainer(pluginInstance);
        this.pluginVer = pluginContainer.getDescription().getVersion().orElse("failed");
        this.pluginName = pluginContainer.getDescription().getName().orElse("sqdplugin");

        server.getEventManager().register(pluginInstance, this);
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
                player.sendRichMessage("<white>Download the plugin update <u><click:open_url:" + downloadUrl + ">here<r>");
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
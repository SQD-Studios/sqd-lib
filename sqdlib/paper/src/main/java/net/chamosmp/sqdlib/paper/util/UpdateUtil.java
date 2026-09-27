package net.chamosmp.sqdlib.paper.util;

import net.chamosmp.sqdlib.internal.InternalUpdater;
import net.chamosmp.sqdlib.util.log.LogType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

/**
 * A utility class for showing your users that the plugin has an update available
 *
 * @apiNote Only supports Modrinth
 * @see UpdateUtil#versionCheck()
 * @see UpdateUtil#onConnect(PlayerJoinEvent)
 */
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
     * @see UpdateUtil#onConnect(PlayerJoinEvent)
     */
    public UpdateUtil(Plugin plugin, String modrinthId, String downloadUrl) {
        this.plugin = plugin;
        this.mrId = modrinthId;
        this.downloadUrl = downloadUrl;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    /**
     * An {@link EventHandler} listener, that listens to {@link PlayerJoinEvent}, to
     * send a message to an admin (Found by the permission {@code (plugin name).update})
     * that an update is available (If an update is found)
     *
     * @param event The event
     * @apiNote You do not need to register this class as a listener
     */
    @EventHandler
    public void onConnect(PlayerJoinEvent event) {
        InternalUpdater.remoteVer(mrId).whenComplete((version, _) -> {
            Player player = event.getPlayer();

            if (version == null || version.equals("failed")) {
                return;
            }

            if (InternalUpdater.isNewerVersion(plugin.getPluginMeta().getVersion(), version) && player.hasPermission(plugin.getPluginMeta().getName().toLowerCase() + ".update")) {
                SchedulerUtil.runForEntity(plugin, player, () -> {
                    player.sendRichMessage("<white>Download the plugin update <u><click:open_url:" + downloadUrl + ">here<r>");
                }, () -> {
                });
            }
        });
    }

    @Override
    public void versionCheck() {
        InternalUpdater.remoteVer(mrId).whenComplete((version, _) -> {
            String pluginVer = plugin.getPluginMeta().getVersion();

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
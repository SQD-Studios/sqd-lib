package net.chamosmp.sqdlib.fabric.util;

import net.chamosmp.sqdlib.fabric.FabricMod;
import net.chamosmp.sqdlib.internal.InternalUpdater;

/**
 * A utility class for showing your users that the plugin has an update available
 *
 * @apiNote Only supports Modrinth. You also don't need to register this as a listener
 * @see UpdateUtil#versionCheck()
 */
public class UpdateUtil implements InternalUpdater {

    private final String mrId;
    private final String downloadUrl;

    private final String pluginVer;

    /**
     * A utility class for showing your users that the plugin has an update available
     *
     * @param mod         The mod instance
     * @param modrinthId  The Modrinth project id (Or slug)
     * @param downloadUrl The download url
     * @apiNote Only supports Modrinth. You also don't need to register this as a listener
     * @see UpdateUtil#versionCheck()
     */
    public UpdateUtil(FabricMod mod, String modrinthId, String downloadUrl) {
        this.mrId = modrinthId;
        this.downloadUrl = downloadUrl;

        this.pluginVer = mod.getModMetadata().getVersion().getFriendlyString();
    }

    @Override
    public void versionCheck() {
        InternalUpdater.remoteVer(mrId).whenCompleteAsync((version, _) -> {
            if (!"failed".equals(version) && !"failed".equals(pluginVer)) {
                if (InternalUpdater.isNewerVersion(pluginVer, version)) {
                    LoggerUtil.getLogger().info("""
                            New update available. Your version: {}, latest version: {}
                            Download plugin here: {}""", pluginVer, version, downloadUrl);
                } else {
                    LoggerUtil.getLogger().info("You are up to date!");
                }
            } else {
                LoggerUtil.getLogger().warn("Failed to check for updates.");
            }
        });
    }
}
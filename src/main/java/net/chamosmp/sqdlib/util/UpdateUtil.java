package net.chamosmp.sqdlib.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateUtil implements Listener {

    private final Plugin plugin;
    private final String mrId;
    private final String downloadUrl;

    /**
     * Class constructor
     *
     * @param plugin The plugin instance
     */
    public UpdateUtil(Plugin plugin, String modrinthId, String downloadUrl) {
        this.plugin = plugin;
        this.mrId = modrinthId;
        this.downloadUrl = downloadUrl;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onConnect(PlayerJoinEvent event) throws IOException, InterruptedException {
        Player player = event.getPlayer();
        String version = remoteVer();

        if (version == null || version.equals("failed")) {
            return;
        }

        if (isNewerVersion(plugin.getPluginMeta().getVersion(), version) && player.hasPermission(plugin.getPluginMeta().getName().toLowerCase() + ".update")) {
            SchedulerUtil.runForEntity(plugin, player, () -> {
                player.sendRichMessage("<white>Download the plugin update <u><click:open_url:" + downloadUrl + ">here<r>");
            }, () -> {
            });
        }
    }

    public void versionCheck() throws Exception {
        String pluginVer = plugin.getPluginMeta().getVersion();
        String version = remoteVer();

        if (!version.equals("failed")) {
            if (isNewerVersion(pluginVer, version)) {
                LoggerUtil.log(LoggerUtil.LogType.INFO, String.format(
                        """
                                New update available. Your version: " + pluginVer + ", latest version: " + version
                                Download plugin here: %s""", downloadUrl
                ));
            }
        } else {
            LoggerUtil.log(LoggerUtil.LogType.WARNING, "Failed to check for updates.");
        }
    }

    public String remoteVer() throws IOException, InterruptedException {
        String baseUrl = "https://api.modrinth.com/v2";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/project/" + mrId + "/version"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();

        Pattern pattern = Pattern.compile("\"version_number\"\\s*:\\s*\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(responseBody);

        if (!matcher.find()) {
            return "failed";
        }

        return matcher.group(1);

    }

    public static boolean isNewerVersion(String current, String latest) {
        String[] currentParts = current.split("\\.");
        String[] latestParts = latest.split("\\.");

        int maxLength = Math.max(currentParts.length, latestParts.length);

        for (int i = 0; i < maxLength; i++) {
            int currentValue = 0;
            int latestValue = 0;
            try {
                currentValue =
                        i < currentParts.length
                                ? Integer.parseInt(currentParts[i])
                                : 0;

                latestValue =
                        i < latestParts.length
                                ? Integer.parseInt(latestParts[i])
                                : 0;
            } catch (NumberFormatException e) {
                LoggerUtil.log(LoggerUtil.LogType.SEVERE, "Had error parsing versions: " + e);
            }
            if (latestValue > currentValue) {
                return true;
            }

            if (latestValue < currentValue) {
                return false;
            }
        }

        return false;
    }

}


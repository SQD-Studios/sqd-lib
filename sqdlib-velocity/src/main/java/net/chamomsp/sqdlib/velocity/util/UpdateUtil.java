package net.chamomsp.sqdlib.velocity.util;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerClientLoadedWorldEvent;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.chamosmp.sqdlib.util.LogType;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateUtil {

    private final ProxyServer server;
    private final String mrId;
    private final String downloadUrl;

    private final Object plugin;

    private final String pluginVer;
    private final String pluginName;

    public UpdateUtil(ProxyServer server, Object pluginInstance, String modrinthId, String downloadUrl) {
        this.server = server;
        this.plugin = pluginInstance;
        this.mrId = modrinthId;
        this.downloadUrl = downloadUrl;

        PluginContainer pluginContainer = server.getPluginManager().ensurePluginContainer(plugin);
        Optional<String> optionalPluginVersion = pluginContainer.getDescription().getVersion();
        this.pluginVer = optionalPluginVersion.orElse("failed");

        Optional<String> optionalPluginName = pluginContainer.getDescription().getName();
        this.pluginName = optionalPluginName.orElse("sqdplugin");

        server.getEventManager().register(pluginInstance, this);
    }

    @Subscribe
    public void onConnect(PlayerClientLoadedWorldEvent event) throws IOException, InterruptedException {
        Player player = event.getPlayer();
        String version = remoteVer();

        if (version == null || version.equals("failed")) {
            return;
        }

        if (isNewerVersion(pluginVer, version) && player.hasPermission(pluginName.toLowerCase() + ".update")) {
            player.sendRichMessage("<white>Download the plugin update <u><click:open_url:" + downloadUrl + ">here<r>");
        }
    }

    public void versionCheck() throws Exception {
        Optional<String> optionalPluginVersion = server.getPluginManager().ensurePluginContainer(plugin).getDescription().getVersion();
        String pluginVer = optionalPluginVersion.orElse("failed");

        String version = remoteVer();

        if (!"failed".equals(version) && !"failed".equals(pluginVer)) {
            if (isNewerVersion(pluginVer, version)) {
                LoggerUtil.log(LogType.INFO, String.format(
                        """
                                New update available. Your version: " + pluginVer + ", latest version: " + version
                                Download plugin here: %s""", downloadUrl
                ));
            }
        } else {
            LoggerUtil.log(LogType.WARNING, "Failed to check for updates.");
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
                LoggerUtil.log(LogType.SEVERE, "Had error parsing versions: " + e);
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


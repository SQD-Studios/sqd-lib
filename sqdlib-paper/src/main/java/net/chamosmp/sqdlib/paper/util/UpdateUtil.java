package net.chamosmp.sqdlib.paper.util;

import net.chamosmp.sqdlib.util.LogType;
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

/**
 * A utility class for showing your users that the plugin has an update available
 *
 * @apiNote Only supports Modrinth
 * @see UpdateUtil#versionCheck()
 * @see UpdateUtil#onConnect(PlayerJoinEvent)
 */
public class UpdateUtil implements Listener {

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
     * @throws IOException          The exception that may throw when running {@link UpdateUtil#remoteVer()}
     * @throws InterruptedException The exception that may throw when running {@link UpdateUtil#remoteVer()}
     * @apiNote You do not need to register this class as a listener
     */
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

    /**
     * Checks for updates, and if an update is available, it sends a message to the console saying the current version and the latest version.
     * If it failed retrieving update information, it prints a warning in the console. If they are running the latest version,
     * it says they're on the latest version
     * <p>
     * You should run this if you want to check for updates and then show it to the console
     *
     * @throws Exception The exception that may throw when running {@link UpdateUtil#remoteVer()}
     */
    public void versionCheck() throws Exception {
        String pluginVer = plugin.getPluginMeta().getVersion();
        String version = remoteVer();

        if (!version.equals("failed")) {
            if (isNewerVersion(pluginVer, version)) {
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
    }

    /**
     * Get the latest version from Modrinth
     *
     * @return the latest version
     * @throws IOException          thrown if an I/O exception occurred or the client is closed  (By {@link HttpClient#send(HttpRequest, HttpResponse.BodyHandler)}
     * @throws InterruptedException thrown if the http request is interrupted (By {@link HttpClient#send(HttpRequest, HttpResponse.BodyHandler)}
     * @apiNote This makes a sync http request everytime you use it, so be careful how you use it
     */
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

    /**
     * Tries to parse, and then return if the version is newer than the current one or not.
     *
     * @param current The current version
     * @param latest  The latest version fetched from modrinth
     * @return {@code true} if the version is newer, {@code false} if it is on the same or newer version than {@code latest}
     */
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


package net.chamosmp.sqdlib.internal;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface InternalUpdater {

    /**
     * Checks for updates, and if an update is available, it sends a message to the console saying the current version and the latest version.
     * If it failed retrieving update information, it prints a warning in the console. If they are running the latest version,
     * it says they're on the latest version
     * <p>
     * You should run this if you want to check for updates and then show it to the console
     *
     */
    void versionCheck();

    /**
     * Get the latest version from Modrinth
     *
     * @return the latest version
     * @apiNote This makes a sync http request everytime you use it, so be careful how you use it
     */
    static CompletableFuture<String> remoteVer(String modrinthId) {
        return CompletableFuture.supplyAsync(() -> {
            try (HttpClient client = HttpClient.newHttpClient()) {
                String baseUrl = "https://api.modrinth.com/v2";

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(baseUrl + "/project/" + modrinthId + "/version"))
                        .build();

                HttpResponse<String> response;

                response = client.send(request, HttpResponse.BodyHandlers.ofString());

                String responseBody = response.body();

                Pattern pattern = Pattern.compile("\"version_number\"\\s*:\\s*\"([^\"]+)\"");
                Matcher matcher = pattern.matcher(responseBody);

                if (!matcher.find()) {
                    return "failed";
                }

                return matcher.group(1);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Tries to parse, and then return if the version is newer than the current one or not.
     *
     * @param current The current version
     * @param latest  The latest version fetched from modrinth
     * @return {@code true} if the version is newer, {@code false} if it is on the same or newer version than {@code latest}
     */
    static boolean isNewerVersion(String current, String latest) {
        String[] currentParts = current.split("\\.");
        String[] latestParts = latest.split("\\.");

        int maxLength = Math.max(currentParts.length, latestParts.length);

        for (int i = 0; i < maxLength; i++) {
            int currentValue;
            int latestValue;
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
                return false;
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

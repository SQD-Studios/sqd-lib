package com.sqdstudios.sqdlib.paper.dialog;

import com.sqdstudios.sqdlib.paper.util.ColorUtil;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

/**
 * A utility class for making dialogs, and doing actions with them
 *
 * @see SimpleDialog#getInput(Component, Player, String, Component, String, Consumer)
 * @see SimpleDialog#getYesNo(Component, Player, Consumer)
 */
public class SimpleDialog {

    /**
     * A utility class for making dialogs, and doing actions with them
     * <p>
     * <p>
     *
     * @param plugin The plugin instance
     * @see SimpleDialog#getInput(Component, Player, String, Component, String, Consumer)
     * @see SimpleDialog#getYesNo(Component, Player, Consumer)
     * @deprecated This method is deprecated because this class no longer needs the plugin instance to work as in internal changes and thus all the methods are now static and don't need to have the {@link SimpleDialog} instance
     */
    @ApiStatus.ScheduledForRemoval(inVersion = "4.2.0")
    @Deprecated(forRemoval = true, since = "4.1.0")
    @SuppressWarnings("unused")
    public SimpleDialog(@Nullable Plugin plugin) {
    }

    /**
     * Opens a text input dialog for a player and calls {@code callback} with the
     * result when the player confirms, or with {@code null} if they discard.
     *
     * @param title    The title of the dialog
     * @param player   The player to open the dialog to
     * @param key      The key of the text input, used to retrieve the value
     * @param content  The label shown next to the text field
     * @param callback Called with the player's input, or null on discard
     */
    @SuppressWarnings("all")
    public static void getInput(@NonNull Component title, @NonNull Player player, @NonNull String key, @NonNull Component content, @Nullable String defaultValue, @NonNull Consumer<String> callback) {
        if (key == null || key.isBlank()) return;

        // Sanitize: lowercase, replace invalid chars with underscores
        final String safeKey = key.toLowerCase().replaceAll("[^a-z0-9_\\-.]", "_");

        Dialog dialog = Dialog.create(builder ->
                builder.empty()
                        .base(DialogBase.builder(title)
                                .inputs(List.of(
                                        DialogInput.text(safeKey, content)
                                                .initial(defaultValue != null ? defaultValue : "")
                                                .build()
                                ))
                                .build()
                        )
                        .type(DialogType.confirmation(
                                ActionButton.create(
                                        ColorUtil.parse("<green>Confirm"),
                                        ColorUtil.parse("Click to confirm your input."),
                                        100,
                                        DialogAction.customClick((dsr, _) -> {
                                            callback.accept(dsr.getText(safeKey));
                                        }, null)
                                ),
                                ActionButton.create(
                                        ColorUtil.parse("<red>Discard"),
                                        ColorUtil.parse("Click to discard your input."),
                                        100,
                                        DialogAction.staticAction(ClickEvent.callback(_ -> {
                                            callback.accept(null);
                                        }))
                                )
                        ))
        );

        player.showDialog(dialog);
    }

    /**
     * Opens a text input dialog for a player and calls {@code callback} with the
     * result when the player confirms, or with {@code null} if they discard.
     *
     * @param title    The title of the dialog
     * @param player   The player to open the dialog to
     * @param callback Called with the player's input, or null on discard
     */
    public static void getYesNo(@NonNull Component title, @NonNull Player player, @NonNull Consumer<Boolean> callback) {
        Dialog dialog = Dialog.create(builder ->
                builder.empty()
                        .base(DialogBase.builder(title)
                                .build()
                        )
                        .type(DialogType.confirmation(
                                ActionButton.create(
                                        ColorUtil.parse("<green>Yes"),
                                        ColorUtil.parse("Click to confirm your input."),
                                        100,
                                        DialogAction.staticAction(ClickEvent.callback(_ -> callback.accept(true)))
                                ),
                                ActionButton.create(
                                        ColorUtil.parse("<red>No"),
                                        ColorUtil.parse("Click to discard your input."),
                                        100,
                                        DialogAction.staticAction(ClickEvent.callback(_ -> callback.accept(false))))
                        ))
        );

        player.showDialog(dialog);
    }
}
package com.sqdstudios.sqdlib.paper.dialog.donutsmp;

import com.sqdstudios.sqdlib.paper.util.ColorUtil;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.action.DialogActionCallback;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jspecify.annotations.Nullable;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A class for building and showing dialogs to players that allow choosing multiple {@link T}s
 *
 * @param <T> The {@link T} represents the type of object you want your players to choose from
 */
public class MultiChoiceDialog<T> {
    private static final ClickCallback.Options CALLBACK_OPTIONS = ClickCallback.Options.builder()
            .uses(1)
            .lifetime(Duration.ofMinutes(10))
            .build();

    private static final int NAME_WIDTH = 150;

    private final Component title;
    private final List<T> objects;
    private final BiConsumer<Audience, List<T>> onContinue;
    private final Function<T, Component> getObjectName;
    private final Predicate<T> predicate;

    private MultiChoiceDialog(Builder<T> b) {
        this.title = b.title;
        this.onContinue = b.onContinue;

        this.objects = b.objs;
        this.getObjectName = b.getName;
        this.predicate = b.predicate;
    }

    public static <T> Builder<T> builder(Component title, List<T> getObjects, Function<T, Component> getObjectName) {
        return new Builder<>(title, getObjects, getObjectName);
    }

    /**
     * Opens the picker with a pre-selection.
     */
    public void open(Audience audience) {
        open(audience, null, null);
    }

    public void open(Audience audience, @Nullable List<T> state, @Nullable String search) {
        audience.showDialog(create(state, search));
    }

    /**
     * Builds the dialog for the given selection state. Each click rebuilds it with the new state.
     */
    public Dialog create(@Nullable List<T> state, @Nullable String search) {
        if (state != null) {
            state = new ArrayList<>(state);
        } else {
            state = new ArrayList<>();
        }

        List<T> finalState = state;
        List<ActionButton> buttons = new ArrayList<>();

        buttons.add(ActionButton.builder(ColorUtil.parse("Search"))
                .action(action((dsr, aud) -> open(aud, finalState, dsr.getText("search"))))
                .build()
        );

        for (T current : objects) {
            if (!predicate.test(current))
                continue;

            if (search != null) {
                if (!ColorUtil.deParse(getObjectName.apply(current)).contains(search)) continue;
            }


            Component component = getObjectName.apply(current);
            if (finalState.contains(current)) {
                component = component.color(NamedTextColor.GREEN);
            }

            buttons.add(
                    ActionButton.create(
                            component,
                            null,
                            NAME_WIDTH,
                            action((_, audience) -> {
                                if (finalState.contains(current)) {
                                    finalState.remove(current);
                                } else {
                                    finalState.add(current);
                                }
                                open(audience, finalState, search);
                            })
                    )
            );
        }

        buttons.add(closeButton());

        DialogBase base = DialogBase.builder(title)
                .canCloseWithEscape(true)
                .inputs(List.of(
                        DialogInput.text("search", ColorUtil.parse("Search:")).build()
                ))
                .build();

        return Dialog.create(builder -> builder.empty()
                .base(base)
                .type(DialogType.multiAction(
                        buttons,
                        confirmButton(finalState),
                        4
                ))
        );
    }

    private ActionButton closeButton() {
        return ActionButton.create(
                ColorUtil.parse("<red>Cancel"),
                null,
                150,
                action((_, audience) -> audience.closeDialog())
        );
    }

    private ActionButton confirmButton(List<T> state) {
        return ActionButton.create(
                ColorUtil.parse("<green>Confirm"),
                null,
                150,
                action((_, audience) -> {
                    audience.closeDialog();
                    onContinue.accept(audience, state);
                })
        );
    }

    private DialogAction action(DialogActionCallback callback) {
        return DialogAction.customClick(callback, CALLBACK_OPTIONS);
    }

    @SuppressWarnings("unused")
    public static final class Builder<T> {

        private final Component title;
        private BiConsumer<Audience, List<T>> onContinue = (_, _) -> {
        };
        private Predicate<T> predicate = _ -> true;

        private final List<T> objs;
        private final Function<T, Component> getName;

        public Builder(Component title, List<T> getObjects, Function<T, Component> getObjectName) {
            this.title = title;
            this.objs = getObjects;
            this.getName = getObjectName;
        }

        /**
         * If the predicate returns true, that means it will be used
         */
        public Builder<T> predicate(Predicate<T> predicate) {
            this.predicate = predicate;
            return this;
        }

        public Builder<T> onChoose(BiConsumer<Audience, List<T>> cb) {
            this.onContinue = cb;
            return this;
        }

        public MultiChoiceDialog<T> build() {
            return new MultiChoiceDialog<>(this);
        }
    }
}

package com.sqdstudios.sqdlib.paper.dialog.donutsmp;

import com.sqdstudios.sqdlib.paper.util.ColorUtil;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.action.DialogActionCallback;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.enchantments.Enchantment;

import java.time.Duration;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;


/**
 * A class for building and showing dialogs to players that allows choosing {@linkplain Enchantment Enchantments}
 */
public class EnchantmentChoiceDialog {
    private static final ClickCallback.Options CALLBACK_OPTIONS = ClickCallback.Options.builder()
            .uses(1)
            .lifetime(Duration.ofMinutes(10))
            .build();

    private static final String[] ROMAN = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};
    private static final int NAME_WIDTH = 150;
    private static final int LEVEL_WIDTH = 28;

    private final Component title;
    private final boolean allowConflicts;
    private final List<Enchantment> enchantments;
    private final int columns;
    private final BiConsumer<Audience, Map<Enchantment, Integer>> onContinue;
    private final Consumer<Audience> onSkip;
    private final boolean allowSkip;

    private EnchantmentChoiceDialog(Builder b) {
        this.title = b.title;
        this.allowConflicts = b.allowConflicts;
        this.onContinue = b.onContinue;
        this.onSkip = b.onSkip;
        this.allowSkip = b.allowSkip;

        this.enchantments = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).stream()
                .filter(b.filter)
                .sorted(Comparator.comparing(e -> e.getKey().asString()))
                .toList();

        int maxLevel = enchantments.stream().mapToInt(Enchantment::getMaxLevel).max().orElse(1);
        this.columns = 1 + maxLevel;
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Opens the picker with nothing selected.
     */
    public void open(Audience audience) {
        open(audience, Map.of());
    }

    /**
     * Opens the picker with a pre-selection.
     */
    public void open(Audience audience, Map<Enchantment, Integer> initial) {
        audience.showDialog(create(initial));
    }

    /**
     * Builds the dialog for the given selection state. Each click rebuilds it with the new state.
     */
    public Dialog create(Map<Enchantment, Integer> selected) {
        final Map<Enchantment, Integer> state = new LinkedHashMap<>(selected);

        List<ActionButton> buttons = new ArrayList<>();
        for (Enchantment enchantment : enchantments) {
            addRow(buttons, enchantment, state);
        }

        if (allowSkip) {
            buttons.add(skipButton());
        }

        buttons.add(continueButton(state));

        DialogBase base = DialogBase.builder(title)
                .canCloseWithEscape(true)
                .build();

        return Dialog.create(builder -> builder.empty()
                .base(base)
                .type(DialogType.multiAction(
                        buttons,
                        null,
                        columns
                ))
        );
    }

    private void addRow(List<ActionButton> out, Enchantment enchantment, Map<Enchantment, Integer> state) {
        Set<Enchantment> conflicts = conflictsWith(enchantment, state);
        boolean conflicting = !conflicts.isEmpty();
        boolean clickable = allowConflicts || !conflicting;
        Integer selectedLevel = state.get(enchantment);

        NamedTextColor nameColor = conflicting ? NamedTextColor.RED
                : selectedLevel != null ? NamedTextColor.GREEN
                : NamedTextColor.WHITE;

        Component nameTooltip = conflicting
                ? ColorUtil.parse("<red>Incompatible with: ").append(
                Component.join(JoinConfiguration.commas(true),
                        conflicts.stream().map(c -> c.description().color(NamedTextColor.RED)).toList()))
                : ColorUtil.parse("");

        out.add(ActionButton.create(enchantment.description().color(nameColor), nameTooltip, NAME_WIDTH, action((_, audience) -> audience.showDialog(create(state)))));

        int max = enchantment.getMaxLevel();
        for (int level = 1; level <= max; level++) {
            final int lvl = level;
            boolean isSelected = selectedLevel != null && selectedLevel == lvl;

            String label = roman(lvl);
            if (conflicting) {
                label = "<red>" + label + "</red>";
            } else if (isSelected) {
                label = "<green>" + label + "</green>";
            } else {
                label = "<white>" + label + "</white>";
            }

            DialogAction action = clickable
                    ? action((_, audience) -> {
                Map<Enchantment, Integer> next = new LinkedHashMap<>(state);
                if (next.get(enchantment) != null && next.get(enchantment) == lvl) {
                    next.remove(enchantment);
                } else {
                    next.put(enchantment, lvl);
                }
                audience.showDialog(create(next));
            })
                    : action((_, audience) -> audience.showDialog(create(state)));

            out.add(ActionButton.create(ColorUtil.parse(label), conflicting ? nameTooltip : null, LEVEL_WIDTH, action));
        }

        for (int i = max + 1; i < columns; i++) {
            out.add(ActionButton.builder(ColorUtil.parse(""))
                    .action(action((_, audience) -> audience.showDialog(create(state))))
                    .width(1)
                    .build()
            );
        }
    }

    private Set<Enchantment> conflictsWith(Enchantment enchantment, Map<Enchantment, Integer> state) {
        Set<Enchantment> result = new LinkedHashSet<>();
        for (Enchantment other : state.keySet()) {
            if (!other.equals(enchantment) && enchantment.conflictsWith(other)) {
                result.add(other);
            }
        }
        return result;
    }

    private ActionButton skipButton() {
        return ActionButton.create(
                ColorUtil.parse("<red>Skip enchantments"),
                ColorUtil.parse(""),
                NAME_WIDTH,
                action((_, audience) -> {
                    audience.closeDialog();
                    onSkip.accept(audience);
                })
        );
    }

    private ActionButton continueButton(Map<Enchantment, Integer> state) {
        return ActionButton.create(
                ColorUtil.parse("<green>Continue"),
                ColorUtil.parse(""),
                150,
                action((_, audience) -> {
                    audience.closeDialog();
                    onContinue.accept(audience, Collections.unmodifiableMap(new LinkedHashMap<>(state)));
                })
        );
    }

    private DialogAction action(DialogActionCallback callback) {
        return DialogAction.customClick(callback, CALLBACK_OPTIONS);
    }

    private String roman(int level) {
        return level >= 1 && level < ROMAN.length ? ROMAN[level] : String.valueOf(level);
    }

    public static final class Builder {

        private Component title = ColorUtil.parse("Choose enchantments");
        private boolean allowConflicts = false;
        private Predicate<Enchantment> filter = _ -> true;
        private BiConsumer<Audience, Map<Enchantment, Integer>> onContinue = (_, _) -> {
        };

        private Consumer<Audience> onSkip = _ -> {
        };

        private boolean allowSkip = true;

        public Builder() {
        }

        public Builder title(Component title) {
            this.title = title;
            return this;
        }

        /**
         * If true, conflicting enchantments are still red but can be selected. Default: false.
         */
        public Builder allowConflicts(boolean allow) {
            this.allowConflicts = allow;
            return this;
        }

        /**
         * Restricts which enchantments are listed (e.g. {@code e -> e.canEnchantItem(item)}).
         */
        public Builder filter(Predicate<Enchantment> filter) {
            this.filter = filter;
            return this;
        }

        public Builder onContinue(BiConsumer<Audience, Map<Enchantment, Integer>> cb) {
            this.onContinue = cb;
            return this;
        }

        public Builder allowSkip(boolean allow) {
            this.allowSkip = allow;
            return this;
        }

        public Builder onSkip(Consumer<Audience> cb) {
            this.onSkip = cb;
            return this;
        }

        public EnchantmentChoiceDialog build() {
            return new EnchantmentChoiceDialog(this);
        }
    }
}

package net.chamosmp.sqdlib.paper.chamogui.config;


import org.bukkit.configuration.ConfigurationSection;

/**
 * Represents the action of a certain Item in a slot
 */
public interface SlotType {
    /**
     * Does nothing (Aka Decoration)
     */
    record Decorative() implements SlotType {
    }

    /**
     * Does a certain action
     *
     * @param action The action
     */
    record ActionSlot(String action) implements SlotType {
    }

    /**
     * Goes to the next page
     */
    record NextPage() implements SlotType {
    }

    /**
     * Goes to the previous page
     */
    record PreviousPage() implements SlotType {
    }

    /**
     * Gets a predefined {@link SlotType}
     *
     * @param type    The type (e.g., ActionSlot)
     * @param section The section holding the item. This only is used on the action slot, where it gets the string action from that section
     * @return The predefined {@link SlotType}
     */
    static SlotType parseSlotType(String type, ConfigurationSection section) {
        return switch (type.toUpperCase()) {
            case "ACTIONSLOT" -> new SlotType.ActionSlot(section.getString("action", ""));
            case "PREVIOUSPAGE" -> new SlotType.PreviousPage();
            case "NEXTPAGE" -> new SlotType.NextPage();
            default -> new SlotType.Decorative();
        };
    }
}
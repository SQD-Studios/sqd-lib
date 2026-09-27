package net.chamosmp.sqdlib.paper.chamogui.config;


/**
 * Represents the action of a certain Item in a slot
 */
public sealed interface SlotType {
    /**
     * Does nothing (Aka Decoration)
     */
    record Decorative() implements SlotType {
    }

    /**
     * Does a certain action
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
}
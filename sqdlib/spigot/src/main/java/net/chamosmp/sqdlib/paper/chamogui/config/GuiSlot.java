package net.chamosmp.sqdlib.paper.chamogui.config;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

/**
 * Definition of a GUI slot from configuration.
 *
 * @param type     The type of the slot.
 * @param slot     The slot index (0-53).
 * @param material The material of the item in this slot.
 * @param name     The display name (MiniMessage).
 * @param lore     The lore (MiniMessage list).
 * @param glow     Whether the item should glow.
 */
public record GuiSlot(
        SlotType type,
        int slot,
        Material material,
        String name,
        List<String> lore,
        boolean glow
) {
    public static List<GuiSlot> parseSlots(ConfigurationSection section) {
        List<GuiSlot> slotsList = new ArrayList<>();
        if (section == null) return slotsList;
        for (String key : section.getKeys(false)) {
            ConfigurationSection s = section.getConfigurationSection(key);
            if (s == null) continue;

            SlotType type = SlotType.parseSlotType(s.getString("type", "Decorative"), s);
            slotsList.add(new GuiSlot(
                    type,
                    s.getInt("slot"),
                    Material.matchMaterial(s.getString("material", "STONE")),
                    s.getString("name", ""),
                    s.getStringList("lore"),
                    s.getBoolean("glow", false)
            ));
        }
        return slotsList;
    }
}
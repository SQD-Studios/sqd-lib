package net.chamosmp.sqdlib.paper.chamogui;

import net.chamosmp.sqdlib.paper.util.ColorUtil;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Fills empty GUI slots with a configurable filler item from config
 *
 * @see GuiFillerUtil#apply(Plugin, Inventory, Player)
 */
public final class GuiFillerUtil {
    private final boolean enabled;
    private final Material material;
    private final String name;
    private final List<String> lore;
    private final boolean glow;

    private GuiFillerUtil(boolean enabled, Material material, String name, List<String> lore, boolean glow) {
        this.enabled = enabled;
        this.material = material;
        this.name = name;
        this.lore = lore;
        this.glow = glow;
    }

    public static @NotNull GuiFillerUtil load(@NotNull FileConfiguration config) {
        ConfigurationSection fillerSection = config.getConfigurationSection("filler");
        if (fillerSection == null) {
            return disabled();
        }

        boolean enabled = fillerSection.getBoolean("filler-item", false);
        if (!enabled) {
            return disabled();
        }

        Material material = Material.GRAY_STAINED_GLASS_PANE;
        String name = " ";
        List<String> lore = Collections.emptyList();
        boolean glow = false;

        if (fillerSection.isConfigurationSection("item")) {
            ConfigurationSection itemSection = fillerSection.getConfigurationSection("item");
            if (itemSection != null) {
                material = Material.matchMaterial(itemSection.getString("material", "GRAY_STAINED_GLASS_PANE"));
                if (material == null || material == Material.AIR) {
                    material = Material.GRAY_STAINED_GLASS_PANE;
                }
                name = itemSection.getString("name", " ");
                lore = itemSection.getStringList("lore");
                glow = itemSection.getBoolean("glow", false);
            }
        } else {
            Material parsed = Material.matchMaterial(fillerSection.getString("item", "GRAY_STAINED_GLASS_PANE"));
            if (parsed != null && parsed != Material.AIR) {
                material = parsed;
            }
        }

        return new GuiFillerUtil(true, material, name, lore, glow);
    }

    private static @NotNull GuiFillerUtil disabled() {
        return new GuiFillerUtil(false, Material.AIR, "", List.of(), false);
    }

    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Places the filler item in every empty slot of the inventory.
     */
    public void fillEmpty(@NotNull Inventory inventory, @Nullable Player player) {
        if (!enabled) return;

        ItemStack filler = createFiller(player);
        for (int slot = 0; slot < inventory.getSize(); slot++) {
            ItemStack existing = inventory.getItem(slot);
            if (existing == null || existing.getType() == Material.AIR) {
                inventory.setItem(slot, filler.clone());
            }
        }
    }

    /**
     * Applies filler to an inventory using the plugin's cached config, or loads directly as fallback.
     */
    public static void apply(@NotNull Plugin plugin, @NotNull Inventory inventory, @Nullable Player player) {
        load(plugin.getConfig()).fillEmpty(inventory, player);
    }

    private @NotNull ItemStack createFiller(@Nullable Player player) {
        ItemStack item = new ItemStack(material);
        var meta = item.getItemMeta();
        if (meta != null) {
            if (player != null) {
                meta.customName(ColorUtil.parse(player, name, Map.of()));
                meta.lore(lore.stream().map(line -> ColorUtil.parse(player, line, Map.of())).toList());
            } else {
                meta.customName(ColorUtil.parse(name));
                meta.lore(lore.stream().map(ColorUtil::parse).toList());
            }
            if (glow) {
                meta.setEnchantmentGlintOverride(true);
            }
            item.setItemMeta(meta);
        }
        return item;
    }
}

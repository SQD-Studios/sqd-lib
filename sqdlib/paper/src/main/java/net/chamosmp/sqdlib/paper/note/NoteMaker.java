package net.chamosmp.sqdlib.paper.note;

import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class NoteMaker implements Listener {
    private List<NoteListener> listeners;

    private static NamespacedKey IS_NOTE;

    public NoteMaker(Plugin plugin) {
        IS_NOTE = new NamespacedKey(plugin, "note");

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void addListener(NoteListener listener) {
        listeners.add(listener);
    }

    public void removeListener(NoteListener listener) {
        listeners.remove(listener);
    }

    public ItemStack createNote(ItemStack startingItem, @Nullable Consumer<PersistentDataContainer> customPdc) {
        if (customPdc != null) {
            startingItem.editPersistentDataContainer(customPdc);
        }

        startingItem.editPersistentDataContainer(pdc -> {
            pdc.set(IS_NOTE, PersistentDataType.BOOLEAN, true);
        });

        return startingItem;
    }

    public ItemStack createNote(ItemStack startingItem) {
        return createNote(startingItem, null);
    }

    @EventHandler
    public void onPlayerClickNote(PlayerInteractEvent event) {
        if (event.getItem() == null) return;

        if (event.getItem().getPersistentDataContainer().has(IS_NOTE, PersistentDataType.BOOLEAN)) {
            listeners.forEach(listener -> {
                listener.onPlayerClickNote(event);
            });
        }
    }

    public interface NoteListener {
        void onPlayerClickNote(PlayerInteractEvent event);
    }
}
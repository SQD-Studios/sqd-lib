import com.mojang.brigadier.Command;
import com.sqdstudios.sqdlib.paper.dialog.SimpleDialog;
import com.sqdstudios.sqdlib.paper.dialog.donutsmp.EnchantmentChoiceDialog;
import com.sqdstudios.sqdlib.paper.dialog.donutsmp.MultiChoiceDialog;
import com.sqdstudios.sqdlib.paper.dialog.donutsmp.OneChoiceDialog;
import com.sqdstudios.sqdlib.paper.util.ColorUtil;
import com.sqdstudios.sqdlib.paper.util.ConfigUtil;
import com.sqdstudios.sqdlib.paper.util.LoggerUtil;
import com.sqdstudios.sqdlib.util.log.LogType;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        new LoggerUtil("CoolPlugin 1| ");
        getServer().getPluginManager().registerEvents(this, this);

        LoggerUtil.log(LogType.INFO, "<green>Cool lil plugin. </green>I like MiniMessage btw");

        ConfigUtil.loadOrAdapt(this, "hi.yml");
        ConfigUtil.loadDataFile(this, "config.yml");

        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            event.registrar().register(
                    Commands.literal("enchantmentsdialog")
                            .executes(i -> {
                                EnchantmentChoiceDialog.builder()
                                        .onContinue((audience, map) -> {
                                            AtomicReference<String> message = new AtomicReference<>("");
                                            map.forEach((key, value) -> message.set(message + "<br>" + ColorUtil.deParse(key.description()) + " " + value));

                                            audience.sendMessage(
                                                    ColorUtil.parse("<green>" + message.get())
                                            );
                                        })
                                        .build()
                                        .open(i.getSource().getSender());
                                return Command.SINGLE_SUCCESS;
                            }).build()
            );

            event.registrar().register(
                    Commands.literal("onechoicedialog")
                            .executes(i -> {
                                OneChoiceDialog.builder(ColorUtil.parse("Choose a material"), Arrays.stream(Material.values()).filter(mat -> !mat.isLegacy()).toList(),
                                                material ->
                                                        Component.translatable(new ItemStack(material)).color(NamedTextColor.WHITE)
                                        )
                                        .onChoose((audience, map) -> audience.sendMessage(
                                                ColorUtil.parse("<green>" + map.name())
                                        ))
                                        .predicate(mat -> mat.isItem() && !mat.isLegacy())
                                        .build()
                                        .open(i.getSource().getSender());
                                return Command.SINGLE_SUCCESS;

                            }).build()
            );

            event.registrar().register(
                    Commands.literal("multichoicedialog")
                            .executes(i -> {
                                MultiChoiceDialog.builder(ColorUtil.parse("Choose a material"), Arrays.stream(Material.values()).filter(mat -> !mat.isLegacy()).toList(),
                                                material ->
                                                        Component.translatable(new ItemStack(material)).color(NamedTextColor.WHITE)
                                        )
                                        .onChoose((audience, map) -> {
                                            StringBuilder message = new StringBuilder();
                                            for (Material mat : map) {
                                                message.append("<green><br>").append(mat.name());
                                            }
                                            audience.sendMessage(
                                                    ColorUtil.parse(message.toString())
                                            );
                                        })
                                        .predicate(mat -> mat.isItem() && !mat.isLegacy())
                                        .build()
                                        .open(i.getSource().getSender());
                                return Command.SINGLE_SUCCESS;

                            }).build()
            );
        });
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.sendMessage(ColorUtil.parse(player, "Hello there!"));

        SimpleDialog.getYesNo(ColorUtil.parse("Yes or no?"), player, b -> {
            if (b) {
                LoggerUtil.log(LogType.INFO, "The player said yes");
            } else {
                LoggerUtil.log(LogType.INFO, "The player said no");
            }
        });
    }
}
import net.chamosmp.sqdlib.paper.util.*;
import net.chamosmp.sqdlib.util.LogType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        new LoggerUtil("CoolPlugin 1| ");
        getServer().getPluginManager().registerEvents(this, this);

        LoggerUtil.log(LogType.INFO, "<green>Cool lil plugin. </green>I like MiniMessage btw");

        ConfigUtil.loadOrAdapt(this, "hi.yml");
        ConfigUtil.loadDataFile(this, "config.yml");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.sendMessage(ColorUtil.parse(player, "Hello there!"));

        new DialogUtil(this).getYesNo(ColorUtil.parse("Yes or no?"), player, b -> {
            if (b) {
                LoggerUtil.log(LogType.INFO, "The player said yes");
                crashPlugin();
            } else {
                LoggerUtil.log(LogType.INFO, "The player said no");
            }
        });
    }

    public void crashPlugin() {
        SchedulerUtil.runAsync(this, () -> {
            Bukkit.getServer().sendMessage(ColorUtil.parse("Oops"));
            Bukkit.getServer().isStopping();
            Bukkit.getCurrentTick();
        });
    }
}
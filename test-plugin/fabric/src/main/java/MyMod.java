import net.chamosmp.sqdlib.fabric.FabricMod;
import net.chamosmp.sqdlib.fabric.util.ConfigUtil;
import net.chamosmp.sqdlib.fabric.util.LoggerUtil;

public class MyMod extends FabricMod {
    public MyMod() {
        super("test-mod");
    }

    @Override
    public void onInitialize() {
        // noinspection all
        new LoggerUtil(this);

        LoggerUtil.getLogger().info("Successfully loaded!");

        getDataDirectory().toFile().mkdirs();
        ConfigUtil.loadOrAdapt(this, "config.yml");
        LoggerUtil.getLogger().info("Config: {}", getConfig().getBoolean("hello", false));

    }
}

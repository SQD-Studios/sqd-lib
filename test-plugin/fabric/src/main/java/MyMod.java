import com.sqdstudios.sqdlib.fabric.FabricMod;
import com.sqdstudios.sqdlib.fabric.util.ConfigUtil;
import com.sqdstudios.sqdlib.fabric.util.LoggerUtil;

public class MyMod extends FabricMod {
    public MyMod() {
        super("test-mod");
    }

    @Override
    public void onInitialize() {
        LoggerUtil.getLogger().info("Successfully loaded!");

        // noinspection all
        getDataDirectory().toFile().mkdirs();
        ConfigUtil.loadOrAdapt(this, "config.yml");
        LoggerUtil.getLogger().info("Config: {}", getConfig().getBoolean("hello", false));

    }
}

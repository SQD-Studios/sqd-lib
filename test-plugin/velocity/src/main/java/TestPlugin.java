import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.sqdstudios.sqdlib.velocity.VelocityPlugin;
import com.sqdstudios.sqdlib.velocity.util.ConfigUtil;
import com.sqdstudios.sqdlib.velocity.util.LoggerUtil;
import com.sqdstudios.sqdlib.util.log.LogType;

import java.nio.file.Path;

@Plugin(
        id = "testplugin",
        name = "My Plugin",
        version = "1.0.0"
)
public class TestPlugin extends VelocityPlugin {

    private final ProxyServer server;

    @Inject
    public TestPlugin(ProxyServer server, @DataDirectory Path configDir) {
        this.server = server;

        super(server, configDir);
    }

    public void onProxyInitialize(ProxyInitializeEvent event) {
        new LoggerUtil("Lil prefix ", server);

        LoggerUtil.log(LogType.INFO, "Hi");

        ConfigUtil.loadOrAdapt(this, "config.yml");
        reloadConfig();
        LoggerUtil.log(LogType.INFO, getConfig().getString("hello"));
    }
}
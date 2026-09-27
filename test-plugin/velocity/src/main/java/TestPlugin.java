import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import net.chamomsp.sqdlib.velocity.VelocityPlugin;
import net.chamomsp.sqdlib.velocity.util.LoggerUtil;
import net.chamosmp.sqdlib.util.log.LogType;
import org.slf4j.Logger;

@Plugin(
        id = "testplugin",
        name = "My Plugin",
        version = "1.0.0"
)
public class TestPlugin extends VelocityPlugin {

    private final ProxyServer server;

    @Inject
    public TestPlugin(ProxyServer server, Logger logger) {
        this.server = server;
    }

    public void onProxyInitialize(ProxyInitializeEvent event) {
        new LoggerUtil("Lil prefix ", server);

        LoggerUtil.log(LogType.INFO, "Hi");
    }
}

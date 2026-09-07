import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import net.chamomsp.sqdlib.velocity.util.LoggerUtil;
import net.chamosmp.sqdlib.util.LogType;
import org.slf4j.Logger;

@Plugin(
        id = "testplugin",
        name = "My Plugin",
        version = "1.0.0"
)
public class TestPlugin {

    private final ProxyServer server;

    @Inject
    public TestPlugin(ProxyServer server, Logger logger) {
        this.server = server;
    }

    @Subscribe
    public void onInit(ProxyInitializeEvent event) {
        new LoggerUtil("Lil prefix", server);

        LoggerUtil.log(LogType.INFO, "Hi");
    }
}

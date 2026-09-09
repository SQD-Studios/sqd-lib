package net.chamomsp.sqdlib.velocity.util;

import com.velocitypowered.api.proxy.ProxyServer;
import org.checkerframework.common.value.qual.IntRange;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * A utility task for scheduling tasks
 */
public final class SchedulerUtil {
    private static final Executor VIRTUAL_THREAD_EXECUTOR = Executors.newVirtualThreadPerTaskExecutor();

    /**
     * You cannot construct {@link SchedulerUtil}, as all of its methods are static
     */
    private SchedulerUtil() {
    }

    /**
     * Get a virtual thread executor
     *
     * @return The {@link Executor}
     */
    public static Executor getVirtualThreadExecutor() {
        return VIRTUAL_THREAD_EXECUTOR;
    }

    /**
     * Run an async task
     *
     * @param server The proxy server
     * @param plugin The plugin object
     * @param task   The task to run
     * @apiNote All tasks in velocity are async, so this is useless
     */
    @ApiStatus.Obsolete
    public static void runAsync(@NotNull ProxyServer server, @NotNull Object plugin, @NotNull Runnable task) {
        server.getScheduler()
                .buildTask(plugin, task)
                .schedule();
    }

    /**
     * Run an async task for a repeated amount of time
     *
     * @param server The proxy server
     * @param plugin The plugin object
     * @param task   The task to run
     * @param time   The time to repeat it for
     * @param unit   The unit to repeat the time for
     */
    public static void runRepeated(@NotNull ProxyServer server, @NotNull Object plugin, @NotNull Runnable task, @IntRange(from = 0L) long time, @NotNull TimeUnit unit) {
        server.getScheduler()
                .buildTask(plugin, task)
                .repeat(time, unit)
                .schedule();
    }

    /**
     * Run an async task delayed
     *
     * @param server The proxy server
     * @param plugin The plugin object
     * @param task   The task to run
     * @param time   The time to delay it for
     * @param unit   The unit to delay the time for
     */
    public static void runDelayed(@NotNull ProxyServer server, @NotNull Object plugin, @NotNull Runnable task, @IntRange(from = 0L) long time, @NotNull TimeUnit unit) {
        server.getScheduler()
                .buildTask(plugin, task)
                .delay(time, unit)
                .schedule();
    }
}
package net.chamomsp.sqdlib.velocity.util;

import com.velocitypowered.api.proxy.ProxyServer;
import org.checkerframework.common.value.qual.IntRange;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public final class SchedulerUtil {
    private static final Executor VIRTUAL_THREAD_EXECUTOR = Executors.newVirtualThreadPerTaskExecutor();

    private SchedulerUtil() {
    }

    public static Executor getVirtualThreadExecutor() {
        return VIRTUAL_THREAD_EXECUTOR;
    }

    public static void runAsync(@NotNull ProxyServer server, @NotNull Object plugin, @NotNull Runnable task) {
        server.getScheduler()
                .buildTask(plugin, task)
                .schedule();
    }

    public static void runRepeated(@NotNull ProxyServer server, @NotNull Object plugin, @NotNull Runnable task, @IntRange(from = 0L) long time, @NotNull TimeUnit unit) {
        server.getScheduler()
                .buildTask(plugin, task)
                .repeat(time, unit)
                .schedule();
    }

    public static void runDelayed(@NotNull ProxyServer server, @NotNull Object plugin, @NotNull Runnable task, @IntRange(from = 0L) long time, @NotNull TimeUnit unit) {
        server.getScheduler()
                .buildTask(plugin, task)
                .delay(time, unit)
                .schedule();
    }
}
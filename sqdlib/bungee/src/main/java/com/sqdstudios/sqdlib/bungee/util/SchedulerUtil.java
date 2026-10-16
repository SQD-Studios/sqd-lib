package com.sqdstudios.sqdlib.bungee.util;

import net.md_5.bungee.api.plugin.Plugin;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Utility for scheduling tasks and getting {@link Executor}s
 */
public final class SchedulerUtil {

    private static final Executor VIRTUAL_THREAD_EXECUTOR = Executors.newVirtualThreadPerTaskExecutor();

    /**
     * You cannot construct {@link SchedulerUtil}, as all of its methods are static
     */
    private SchedulerUtil() {
    }

    /**
     * Gets a virtual thread executor
     *
     * @return the {@link Executor}
     */
    public static Executor getVirtualThreadExecutor() {
        return VIRTUAL_THREAD_EXECUTOR;
    }

    /**
     * Schedules the specified task to be executed asynchronously immediately.
     *
     * @param plugin Plugin which owns the specified task.
     * @param task   specified task
     */
    public static void runAsync(@NonNull Plugin plugin, @NonNull Runnable task) {
        plugin.getProxy().getScheduler().runAsync(plugin, task);
    }

    /**
     * Schedules a task to be executed on the global region after the specified delay in ticks.
     *
     * @param plugin The plugin that owns the task
     * @param task   The task to execute
     * @param delay  The delay
     * @param unit   The time unit of the delay parameter
     */
    public static void runDelayed(@NonNull Plugin plugin, @NonNull Runnable task, long delay, @NonNull TimeUnit unit) {
        plugin.getProxy().getScheduler().schedule(plugin, task, delay, unit);
    }
}

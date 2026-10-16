package net.chamosmp.sqdlib.paper.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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
        Bukkit.getScheduler().runTaskAsynchronously(plugin, _ -> task.run());
    }

    /**
     * Schedules a task to be executed on the global region on the next tick.
     *
     * @param plugin Plugin which owns the task.
     * @param task   The task to execute
     */
    public static void runSync(@NonNull Plugin plugin, @NonNull Runnable task) {
        Bukkit.getScheduler().runTask(plugin, _ -> task.run());
    }

    /**
     * Schedules a task to be executed on the global region after the specified delay in ticks.
     *
     * @param plugin     The plugin that owns the task
     * @param task       The task to execute
     * @param delayTicks The delay, in ticks
     */
    public static void runDelayed(@NonNull Plugin plugin, @NonNull Runnable task, long delayTicks) {
        Bukkit.getScheduler().runTaskLater(plugin, _ -> task.run(), delayTicks);
    }
}

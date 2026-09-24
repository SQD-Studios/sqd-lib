package net.chamosmp.sqdlib.paper.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

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
        Bukkit.getAsyncScheduler().runNow(plugin, _ -> task.run());
    }

    /**
     * Schedules a task to be executed on the global region on the next tick.
     *
     * @param plugin Plugin which owns the task.
     * @param task   The task to execute
     */
    public static void runSync(@NonNull Plugin plugin, @NonNull Runnable task) {
        Bukkit.getGlobalRegionScheduler().run(plugin, _ -> task.run());
    }

    /**
     *
     * Schedules a task to execute on the next tick for an entity.
     * The task failed to schedule because the scheduler is retired (entity removed).
     * Otherwise, either the task callback will be invoked, or the retired callback will be invoked if the scheduler is retired.
     * Note that the retired callback is invoked in critical code, so it should not attempt to remove the entity, remove other entities, load chunks, load worlds, modify ticket levels, etc.
     * It is guaranteed that the task and retired callback are invoked on the region which owns the entity
     *
     * @param plugin  The plugin that owns the task
     * @param entity  The entity to run the task on
     * @param task    The task to execute
     * @param retired Retire callback to run if the entity is retired before the run callback can be invoked, may be null.
     */
    public static void runForEntity(@NonNull Plugin plugin, @NonNull Entity entity, @NonNull Runnable task, @Nullable Runnable retired) {
        entity.getScheduler().run(plugin, _ -> task.run(), retired);
    }

    /**
     * Schedules a task to be executed on the region which owns the location on the next tick.
     *
     * @param plugin   The plugin that owns the task
     * @param location The location at which the region executing should own
     * @param task     The task to execute
     */
    public static void runAtLocation(@NonNull Plugin plugin, @NonNull Location location, @NonNull Runnable task) {
        Bukkit.getRegionScheduler().run(plugin, location, _ -> task.run());
    }

    /**
     * Schedules a task to be executed on the global region after the specified delay in ticks.
     *
     * @param plugin     The plugin that owns the task
     * @param task       The task to execute
     * @param delayTicks The delay, in ticks
     */
    public static void runDelayed(@NonNull Plugin plugin, @NonNull Runnable task, long delayTicks) {
        Bukkit.getGlobalRegionScheduler().runDelayed(plugin, _ -> task.run(), delayTicks);
    }
}

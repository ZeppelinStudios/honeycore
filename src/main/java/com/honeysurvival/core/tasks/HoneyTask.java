package com.honeysurvival.core.tasks;

import com.honeysurvival.core.component.ComponentManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * HoneyTask is an extension of BukkitTask involving custom
 * functionality and timeout periods.
 */
public abstract class HoneyTask extends BukkitRunnable {
    private final JavaPlugin plugin;
    private final ComponentManager componentManager;

    protected HoneyTask(JavaPlugin plugin, ComponentManager componentManager) {
        this.plugin = plugin;
        this.componentManager = componentManager;
    }
}

package com.honeysurvival.core.integration;

import lombok.Getter;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;
import java.util.logging.Logger;

@Getter
public abstract class CoreIntegration<T> implements ICoreIntegration {
    private final JavaPlugin plugin;
    private final Logger logger;
    private final Server server;

    public CoreIntegration(JavaPlugin plugin, Logger logger, Server server) {
        this.plugin = plugin;
        this.logger = logger;
        this.server = server;
    }

    public abstract Optional<T> get();
}

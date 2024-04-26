package com.honeysurvival.core.integration.integrations;

import com.Zrips.CMI.CMI;
import com.honeysurvival.core.integration.CoreIntegration;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;
import java.util.logging.Logger;

public class CMIBaseIntegration extends CoreIntegration<CMI> {
    protected CMI cmi;

    public CMIBaseIntegration(JavaPlugin plugin, Logger logger, Server server) {
        super(plugin, logger, server);
    }

    @Override
    public Optional<CMI> get() {
        return Optional.of(cmi);
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    public void onEnable() {
        if (getServer().getPluginManager().isPluginEnabled("CMI")) {
            this.cmi = CMI.getInstance();
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}

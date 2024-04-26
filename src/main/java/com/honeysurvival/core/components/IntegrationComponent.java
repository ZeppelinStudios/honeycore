package com.honeysurvival.core.components;

import com.honeysurvival.core.component.PluginComponent;
import com.honeysurvival.core.integration.CoreIntegration;
import com.honeysurvival.core.integration.ICoreIntegration;
import com.honeysurvival.core.integration.integrations.CMIBaseIntegration;
import lombok.Getter;
import org.bukkit.entity.LightningStrike;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Getter
public class IntegrationComponent implements PluginComponent {
    protected final List<CoreIntegration<?>> integrations;

    public IntegrationComponent(JavaPlugin plugin) {
        this.integrations = new ArrayList<>();

        integrations.add(
                new CMIBaseIntegration(plugin, plugin.getLogger(), plugin.getServer())
        );
    }

    @Override
    public void onLoad() {
        integrations.forEach(ICoreIntegration::onLoad);
    }

    @Override
    public void onEnable() {
        integrations.forEach(ICoreIntegration::onEnable);
    }

    @Override
    public void onDisable() {
        integrations.forEach(ICoreIntegration::onDisable);
    }

    public Optional<CoreIntegration<?>> classSearch(Class<?> clazz) {
       return getIntegrations().stream().filter(c -> c.getClass().equals(clazz)).findAny();
    }
}

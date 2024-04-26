package com.honeysurvival.core;

import com.honeysurvival.core.commandapi.CommandApiComponent;
import com.honeysurvival.core.component.ComponentManager;
import com.honeysurvival.core.components.IntegrationComponent;
import com.honeysurvival.core.components.ReportComponent;
import com.honeysurvival.core.config.MainConfig;
import com.honeysurvival.core.config.MessagesConfig;
import com.honeysurvival.core.config.ReportDataConfig;
import com.honeysurvival.core.integration.integrations.CMIBaseIntegration;
import com.honeysurvival.core.loaders.ReportLoader;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import dev.jorel.commandapi.CommandAPIConfig;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

@Getter
public final class CorePlugin extends JavaPlugin {

    private ComponentManager componentManager;

    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this).silentLogs(true));

        this.componentManager = ComponentManager.register(this, List.of(
                MainConfig.createDefault(this),
                MessagesConfig.createDefault(this),
                ReportDataConfig.createDefault(this),
                new IntegrationComponent(this),
                new ReportLoader(this),
                new ReportComponent(this)
        ));

        componentManager.loadComponents();
    }

    @Override
    public void onEnable() {
        CommandAPI.onEnable();

        componentManager.enableComponents();
    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();

        componentManager.disableComponents();
    }

    public MessagesConfig getMessages() {
        return componentManager.getComponent(MessagesConfig.class);
    }
}

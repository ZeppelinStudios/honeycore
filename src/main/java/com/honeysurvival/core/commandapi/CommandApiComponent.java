package com.honeysurvival.core.commandapi;

import com.honeysurvival.core.component.BasePluginComponent;
import com.honeysurvival.core.component.PluginComponent;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandApiComponent implements PluginComponent {

    private JavaPlugin plugin;

    public CommandApiComponent(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIBukkitConfig(plugin).silentLogs(true));
    }

    @Override
    public void onEnable() {
        CommandAPI.onEnable();
    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();
    }
}

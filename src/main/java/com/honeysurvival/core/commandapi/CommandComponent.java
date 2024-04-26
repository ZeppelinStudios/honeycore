package com.honeysurvival.core.commandapi;

import com.honeysurvival.core.component.BasePluginComponent;
import com.honeysurvival.core.component.PluginComponent;
import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * A plugin component that implements a command using the CommandAPI.
 */
public abstract class CommandComponent implements PluginComponent {

    private JavaPlugin plugin;
    private final CommandAPICommand command;

    public CommandComponent(JavaPlugin plugin) {
        this.plugin = plugin;
        this.command = createCommand();
    }

    public abstract CommandAPICommand createCommand();

    @Override
    public void onEnable() {
        command.register();
    }
}




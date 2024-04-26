package com.honeysurvival.core.config;

import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.logging.Logger;

public class MainConfig extends BaseConfig {

    public MainConfig(File file, String defaultName, Logger logger) {
        super(file, defaultName, logger);
    }


    public static MainConfig createDefault(Plugin plugin) {
        return new MainConfig(new File(plugin.getDataFolder(), "config.yml"), "/config/config.yml", plugin.getLogger());
    }

}

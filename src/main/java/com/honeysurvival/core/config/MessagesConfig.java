package com.honeysurvival.core.config;

import com.honeysurvival.core.CoreMessage;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.logging.Logger;

public class MessagesConfig extends BaseConfig {

    public MessagesConfig(File file, String defaultName, Logger logger) {
        super(file, defaultName, logger);
    }

    public CoreMessage getMessage(String key) {
        return new CoreMessage(configuration.getString(key));
    }

    public YamlConfiguration getConfig() {
        return configuration;
    }

    public static MessagesConfig createDefault(Plugin plugin) {
        return new MessagesConfig(new File(plugin.getDataFolder(), "messages.yml"), "/config/messages.yml", plugin.getLogger());
    }

}

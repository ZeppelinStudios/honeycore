package com.honeysurvival.core.config;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

public class XrayConfig extends BaseConfig {

    public XrayConfig(File file, String defaultName, Logger logger) {
        super(file, defaultName, logger);
    }

    public static XrayConfig createDefault(Plugin plugin) {
        return new XrayConfig(new File(plugin.getDataFolder(), "xray.yml"), "/config/components/xray.yml", plugin.getLogger());
    }

    public List<Material> getMaterials() {
        return configuration.getStringList("materials").stream().map(Material::valueOf).toList();
    }

    public FileConfiguration getConfig() {
        return this.configuration;
    }

}

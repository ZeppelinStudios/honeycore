package com.honeysurvival.core.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.logging.Logger;

public class ReportDataConfig extends BaseConfig {

    public ReportDataConfig(File file, String defaultName, Logger logger) {
        super(file, defaultName, logger);
    }

    public static ReportDataConfig createDefault(Plugin plugin) {
        return new ReportDataConfig(new File(plugin.getDataFolder(), "reportData.yml"), "/config/reportData.yml", plugin.getLogger());
    }

    public FileConfiguration getConfig() {
        return this.configuration;
    }

}

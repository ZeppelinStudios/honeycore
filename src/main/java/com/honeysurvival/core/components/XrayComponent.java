package com.honeysurvival.core.components;

import com.honeysurvival.core.CorePlugin;
import com.honeysurvival.core.component.PluginComponent;
import com.honeysurvival.core.loaders.MineClusterLoader;
import org.bukkit.event.Listener;

public class XrayComponent implements PluginComponent, Listener {
    private CorePlugin plugin;
    private MineClusterLoader clusterLoader;

    public XrayComponent(CorePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onEnable() {
        this.clusterLoader = plugin.getComponentManager().getComponent(MineClusterLoader.class);
    }





}

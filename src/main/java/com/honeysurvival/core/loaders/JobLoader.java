package com.honeysurvival.core.loaders;

import com.honeysurvival.core.models.Job;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class JobLoader implements CoreLoader<Job> {

    private JavaPlugin plugin;
    public JobLoader(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void save(Job job) {

    }

    @Override
    public Optional<Job> load(String id) {
        return Optional.empty();
    }

    @Override
    public List<Job> all() {
        return List.of();
    }

    @Override
    public List<Job> filtered(Predicate<Job> filter) {
        return List.of();
    }
}

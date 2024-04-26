package com.honeysurvival.core.loaders;

import com.honeysurvival.core.CorePlugin;
import com.honeysurvival.core.component.ComponentManager;
import com.honeysurvival.core.config.ReportDataConfig;
import com.honeysurvival.core.models.Report;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public class ReportLoader implements CoreLoader<Report> {
    private CorePlugin plugin;
    private ReportDataConfig reportDataConfig;

    @Override
    public void onEnable() {
        this.reportDataConfig = ComponentManager.getComponentManager(plugin).getComponent(ReportDataConfig.class);
        if (reportDataConfig == null) {
            plugin.getLogger().warning("Attempted to load ReportDataConfig but could not as it is null.");
            return;
        }
    }

    public ReportLoader(CorePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void save(Report report) {
        reportDataConfig.getConfig().set("reports." + report.getUuid(), report.toString());
        reportDataConfig.save();
    }

    @Override
    public Optional<Report> load(String uuid) {
        if (UUID.fromString(uuid) != null) {
            String data = reportDataConfig.getConfig().getString("reports." + uuid);

            String[] parts = data.split(",");

            UUID uniqueId = UUID.fromString(parts[0]);
            UUID reporter = UUID.fromString(parts[1]);
            UUID reported = UUID.fromString(parts[2]);
            String reason = parts[3];
            boolean open = Boolean.parseBoolean(parts[4]);

            return Optional.of(new Report(uniqueId, reporter, reported, reason, open));
        }
        return Optional.empty();
    }

    @Override
    public List<Report> all() {
        List<Report> reports = new ArrayList<>();

        reportDataConfig.getConfig().getConfigurationSection("reports").getKeys(false).stream().forEach(reportId -> {
            Optional<Report> report = load(reportId);
            report.ifPresent(reports::add);
        });


        return reports;
    }

    @Override
    public List<Report> filtered(Predicate<Report> filter) {
        return all().stream().filter(filter).toList();
    }
}

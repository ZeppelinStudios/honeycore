package com.honeysurvival.core.components;

import com.honeysurvival.core.CoreMessage;
import com.honeysurvival.core.CorePlugin;
import com.honeysurvival.core.commandapi.CommandComponent;
import com.honeysurvival.core.loaders.ReportLoader;
import com.honeysurvival.core.models.Report;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.PlayerArgument;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class ReportComponent extends CommandComponent implements Listener {

    private CorePlugin plugin;
    private HashMap<UUID, Report> cachedReports;

    public ReportComponent(CorePlugin plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @Override
    public void onEnable() {
        this.cachedReports = new HashMap<>();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public CommandAPICommand createCommand() {
        CommandAPICommand create = new CommandAPICommand("create")
                .withArguments(
                        new PlayerArgument("target").withRequirement(Objects::nonNull)
                )
                .executesPlayer((player, args) -> {
                    if (cachedReports.containsKey(player.getUniqueId())) {
                        String targetDisplayName = Bukkit.getPlayer(cachedReports.get(player.getUniqueId()).getReported()).getName();
                        player.sendMessage(
                                plugin.getMessages().getMessage("report-duplicate")
                                        .defaultPlaceholders(player.getName(), targetDisplayName)
                                        .hex()
                                        .color()
                                        .toString()
                        );

                        return;
                    }

                    Player target = (Player) args.get("target");

                    Report report = new Report(
                            UUID.randomUUID(),
                            player.getUniqueId(),
                            target.getUniqueId(),
                            "",
                            false
                    );

                    cachedReports.put(player.getUniqueId(), report);

                    player.sendMessage(
                            plugin.getMessages().getMessage("report-started")
                                    .defaultPlaceholders(player.getName(), target.getName())
                                    .hex()
                                    .color()
                                    .toString()
                    );
                });

        CommandAPICommand confirm = new CommandAPICommand("confirm")
                .executesPlayer((player, args) -> {
                   if (!cachedReports.containsKey(player.getUniqueId()) || cachedReports.get(player.getUniqueId()).getReason().isEmpty()) {
                       player.sendMessage(
                               plugin.getMessages().getMessage("report-confirm-failed")
                                       .defaultPlaceholders(player.getName(), "")
                                       .color()
                                       .hex()
                                       .toString()
                       );
                       return;
                   }

                    Report report = cachedReports.get(player.getUniqueId());
                    ReportLoader reportLoader = plugin.getComponentManager().getComponent(ReportLoader.class);

                    reportLoader.save(report);
                    player.sendMessage(
                            plugin.getMessages().getMessage("report-sent")
                                    .defaultPlaceholders(player.getName(), Bukkit.getPlayer(report.getReported()).getName())
                                    .placeholder("%report_reason%", report.getReason())
                                    .placeholder("%report_id%", report.getUuid().toString())
                                    .hex()
                                    .color()
                                    .toString()
                    );

                    // TODO: Send to staff
                });

        CommandAPICommand cancel = new CommandAPICommand("cancel")
                .executesPlayer((player, args) -> {
                    if (cachedReports.containsKey(player.getUniqueId())) {
                        player.sendMessage(
                                plugin.getMessages().getMessage("report-cancelled")
                                        .defaultPlaceholders(player.getName(),
                                                Bukkit.getPlayer(cachedReports.get(player.getUniqueId()).getReported()).getName())
                                        .hex()
                                        .color()
                                        .toString()
                        );
                        cachedReports.remove(player.getUniqueId());
                    }
                });


        return new CommandAPICommand("report")
                .withShortDescription("Create a player report")
                .withSubcommands(create, confirm,  cancel)
                .executesPlayer(((player, commandArguments) -> {
                    plugin.getMessages().getConfig().getStringList("report-help").forEach(string -> {
                        player.sendMessage(new CoreMessage(string).hex().color().toString());
                    });
                }));

    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (cachedReports.containsKey(event.getPlayer().getUniqueId()) && cachedReports.get(event.getPlayer().getUniqueId()).getReason().isEmpty()) {
            Report cached = cachedReports.get(event.getPlayer().getUniqueId());

            cached.setReason(event.getMessage());

            cachedReports.put(event.getPlayer().getUniqueId(), cached);

            event.getPlayer().sendMessage(
                    plugin.getMessages().getMessage("report-confirm")
                            .defaultPlaceholders(event.getPlayer().getName(), Bukkit.getPlayer(cached.getReported()).getName())
                            .color()
                            .hex()
                            .toString()
            );
        }
    }
}

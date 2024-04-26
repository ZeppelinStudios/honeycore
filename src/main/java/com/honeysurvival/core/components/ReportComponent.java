package com.honeysurvival.core.components;

import com.honeysurvival.core.CoreMessage;
import com.honeysurvival.core.CorePlugin;
import com.honeysurvival.core.commandapi.CommandComponent;
import com.honeysurvival.core.config.MainConfig;
import com.honeysurvival.core.loaders.ReportLoader;
import com.honeysurvival.core.models.Report;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.*;

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
        createCommand().register();
    }

    @Override
    public CommandAPICommand createCommand() {
        CommandAPICommand close = new CommandAPICommand("close")
                .withArguments(
                        new StringArgument("id"),
                        new BooleanArgument("positive")
                )
                .executesPlayer((player, args) -> {
                    String id = (String) args.get("id");
                    boolean positive = (Boolean) args.get("positive");

                    Report report = plugin.getComponentManager().getComponent(ReportLoader.class).load(id).orElse(null);
                    report.setOpen(false);


                    Player executor = Bukkit.getPlayer(report.getReporter());
                    Player target = Bukkit.getPlayer(report.getReported());

                    player.sendMessage(plugin.getMessages().getMessage("report-closed").color().toString());

                    if (positive) {
                        plugin.getMessages().getConfig().getStringList("report-status-positive").forEach(msg -> {
                            String m = new CoreMessage(msg)
                                    .defaultPlaceholders(executor.getName(), target.getName())
                                    .placeholder("%report_reason%", report.getReason())
                                    .placeholder("%report_id%", report.getUuid())
                                    .color()
                                    .hex()
                                    .toString();

                            player.sendMessage(m);
                        });
                    } else {
                        plugin.getMessages().getConfig().getStringList("report-status-negative").forEach(msg -> {
                            String m = new CoreMessage(msg)
                                    .defaultPlaceholders(executor.getName(), target.getName())
                                    .placeholder("%report_reason%", report.getReason())
                                    .placeholder("%report_id%", report.getUuid())
                                    .color()
                                    .hex()
                                    .toString();

                            player.sendMessage(m);
                        });
                    }

                });


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
                                    .color()
                                    .hex()
                                    .toString()
                    );
                });


        CommandAPICommand confirm = new CommandAPICommand("confirm")
                .executesPlayer((player, args) -> {
                    if (!cachedReports.containsKey(player.getUniqueId()) || cachedReports.get(player.getUniqueId()).getReason().isEmpty()) {
                        player.sendMessage(
                                plugin.getMessages().getMessage("report-confirm-failed")
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
                    String permission = plugin.getComponentManager().getComponent(MainConfig.class).getReportStaffPermission();

                    ArrayList<Player> onlineStaff = new ArrayList<>(plugin.getServer().getOnlinePlayers());
                    onlineStaff.removeIf(player1 -> !player.hasPermission(permission) || !player.isOp());

                    onlineStaff.forEach(staffMember -> {
                        plugin.getMessages().getConfig().getStringList("report-received-staff").forEach(staffMsg -> {
                            String msg = new CoreMessage(staffMsg)
                                    .defaultPlaceholders(
                                            player.getName(),
                                            Bukkit.getPlayer(report.getReported()).getName()
                                    )
                                    .placeholder("%report_reason%", report.getReason())
                                    .placeholder("%report_id%", report.getUuid())
                                    .hex()
                                    .color()
                                    .toString();

                            staffMember.sendMessage(msg);
                        });

                        String targetName = Bukkit.getPlayer(report.getReported()).getName();
                        String reportId = report.getUuid().toString();

                        Component teleport = MiniMessage.miniMessage().deserialize(plugin.getMessages().getConfig().getString("report-teleport"))
                                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + targetName))
                                .hoverEvent(HoverEvent.showText(Component.text("Teleport to target")));
                        Component reportClosePositive = MiniMessage.miniMessage().deserialize(plugin.getMessages().getConfig().getString("report-close-positive"))
                                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/report close " + reportId + " true"))
                                .hoverEvent(HoverEvent.showText(Component.text("Click to close report with Positive feedback. (No action taken)")));
                        Component reportCloseNegative = MiniMessage.miniMessage().deserialize(plugin.getMessages().getConfig().getString("report-close-negative"))
                                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/report close " + reportId + " false"))
                                .hoverEvent(HoverEvent.showText(Component.text("Click to close report with Negative feedback. (No action taken)")));

                        staffMember.sendMessage(teleport);
                        staffMember.sendMessage(reportClosePositive);
                        staffMember.sendMessage(reportCloseNegative);

                        staffMember.sendMessage(
                                plugin.getMessages().getMessage("report-panel-bottom").color().hex().toString()
                        );


                    });
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
                .withSubcommands(create, confirm, cancel, close)
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

            event.setCancelled(true);

            plugin.getMessages().getConfig().getStringList("report-ready").forEach(readyMsg -> {
                String msg = new CoreMessage(readyMsg)
                        .defaultPlaceholders(event.getPlayer().getName(), Bukkit.getPlayer(cachedReports.get(event.getPlayer().getUniqueId()).getReported()).getName())
                        .placeholder("%report_reason%", cached.getReason())
                        .placeholder("%report_id%", cached.getUuid())
                        .color()
                        .hex()
                        .toString();

                event.getPlayer().sendMessage(msg);
            });

            Component confirm = MiniMessage.miniMessage().deserialize(plugin.getMessages().getConfig().getString("report-confirm-action"))
                    .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/report confirm"))
                    .hoverEvent(HoverEvent.showText(Component.text("Your report will be sent and stored.")));
            Component cancel = MiniMessage.miniMessage().deserialize(plugin.getMessages().getConfig().getString("report-cancel-action"))
                    .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/report cancel"))
                    .hoverEvent(HoverEvent.showText(Component.text("Your report will be thrown out and not sent. Cannot be undone.")));

            event.getPlayer().sendMessage(confirm);
            event.getPlayer().sendMessage(cancel);

            event.getPlayer().sendMessage(
                    plugin.getMessages().getMessage("report-bottom")
                            .color()
                            .hex()
                            .toString()
            );
        }
    }
}

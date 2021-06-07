package fr.gonzyui.managers;


import fr.gonzyui.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.chat.TextComponent;

import java.awt.*;
import java.util.ArrayList;

public class ReportManager {
    private ArrayList<ProxiedPlayer> reportToggle = new ArrayList<>();

    private ArrayList<ProxiedPlayer> reportedPlayer = new ArrayList<>();

    public ArrayList<ProxiedPlayer> getReportTogglePlayers() {
        return reportToggle;
    }

    public ArrayList<ProxiedPlayer> getReportedPlayer() {
        return reportedPlayer;
    }

    public boolean getServersDisableNotifyMessage(ProxiedPlayer player) {
        for (final String servers : Main.getPlugin().getConfig().getStringList("CANCEL-SERVERS.NOTIFY-MESSAGE")) {
            if (servers.equalsIgnoreCase(player.getServer().getInfo().getName())) {
                return true;
            }
        }
        return false;
    }

    public boolean getServersDisableReportCommand(ProxiedPlayer player) {
        for (final String servers : Main.getPlugin().getConfig().getStringList("CANCEL-SERVERS.REPORT-COMMAND")) {
            if (player.getServer().getInfo().getName().equalsIgnoreCase(servers)) {
                return true;
            }
        }
        return false;
    }

    public boolean getReportReasons(String reason) {
        for (final String reasons : Main.getPlugin().getConfig().getStringList("REPORT.REASONS.LIST")) {
            if (reason.equalsIgnoreCase(reasons)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("deorecation")
    public void getReportMessage(ProxiedPlayer players, ProxiedPlayer player, ProxiedPlayer target, String message) {
        for (String report : Main.getPlugin().getConfig().getStringList("REPORT.NOTIFY-MESSAGE")) {
            report = report.replace("<player>", player.getName());
            report = report.replace("<target>", target.getName());
            report = report.replace("<server>", target.getServer().getInfo().getName());
            report = report.replace("<reason>", message);
            final TextComponent clickMessage = new TextComponent(ChatColor.translateAlternateColorCodes('&', report));
            clickMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', Main.getPlugin().getConfig().getString("REPORT.HOVER.MESSAGE")).replace("<player", player.getName()).replace("<server>", player.getServer().getInfo().getName())).create()));
            clickMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, Main.getPlugin().getConfig().getString("REPORT.HOVER.COMMAND").replace("<server>", player.getServer().getInfo().getName())));
            players.sendMessage(clickMessage);
        }
    }

    public void getReportTitle(ProxiedPlayer players, ProxiedPlayer player, ProxiedPlayer target) {
        if (Main.getPlugin().getConfig().getBoolean("REPORT.TITLE.ENABLED")) {
            final Title reportTitle = ProxyServer.getInstance().createTitle().title(new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', Main.getPlugin().getConfig().getString("REPORT.TITLE.LINES.1"))).create()).subTitle(new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', Main.getPlugin().getConfig().getString("REPORT.TITLE.LINES.2")).replace("<player>", player.getName()).replace("<target>", target.getName())).create());
            players.sendTitle(reportTitle);
        }
    }

    public void getReportCooldown(ProxiedPlayer player, ProxiedPlayer target) {
        final int REPORT_COOLDOWN = Main.getPlugin().getConfig().getInt("REPORT.COOLDOWN");
        CooldownManager.addCooldown("cooldown", player, REPORT_COOLDOWN);
        for (String success : Main.getPlugin().getConfig().getStringList("REPORT.USAGE.SUCCESSFULLY")) {
            player.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', success).replace("<target>", target.getName())));
        }
    }

    public void getPlayersReported(ProxiedPlayer player) {
        for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
            if (getReportedPlayer().contains(players)) {
                player.sendMessage(new TextComponent(players.getName()));
            }
        }
    }

}

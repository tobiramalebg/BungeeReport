package fr.gonzyui.commands;

import fr.gonzyui.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ReportListCmd extends Command {
    public ReportListCmd() {
        super("reports", "", new String[] { "breports" });
    }

    public void execute(final CommandSender sender, final String[] args) {
        if (!sender.hasPermission("breport.reports")) {
            sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', Main.getPlugin().getConfig().getString("OTHERS.NO-PERMISSIONS"))));
            return;
        } else if(Main.getPlugin().getReport().getReportedPlayer().size() == 0) {
            sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', Main.getPlugin().getConfig().getString("OTHERS.NO-REPORTS"))));
        } else {
            sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&3&lReports &f\u2503 Players reported online.")));
            sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "")));
            for(ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
                if(Main.getPlugin().getReport().getReportedPlayer().contains(players)) {
                    sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&3"+players+":")));
                    sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&f  Server: &b"+players.getServer().getInfo().getName())));
                }
            }
        }
        return;
    }
}

package fr.gonzyui.commands;

import fr.gonzyui.Main;
import net.md_5.bungee.api.plugin.*;
import net.md_5.bungee.api.connection.*;
import net.md_5.bungee.api.*;
import net.md_5.bungee.api.chat.*;

public class ToggleReports extends Command {
    public ToggleReports() {
        super("togglereport", "", new String[] { "togglereports", "reporttoggle", "reportstoggle" });
    }

    public void execute(final CommandSender sender, final String[] args) {
        final ProxiedPlayer player = (ProxiedPlayer)sender;
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage((BaseComponent)new TextComponent(ChatColor.translateAlternateColorCodes('&', Main.getPlugin().getConfig().getString("OTHERS.NO-CONSOLE"))));
            return;
        }
        else if (!player.hasPermission("breport.toggle")) {
            player.sendMessage((BaseComponent)new TextComponent(ChatColor.translateAlternateColorCodes('&', Main.getPlugin().getConfig().getString("OTHERS.NO-PERMISSIONS"))));
            return;
        }
        else if (!Main.getPlugin().getReport().getReportTogglePlayers().contains(player)) {
            Main.getPlugin().getReport().getReportTogglePlayers().add(player);
            player.sendMessage((BaseComponent)new TextComponent(ChatColor.translateAlternateColorCodes('&', Main.getPlugin().getConfig().getString("REPORT.TOGGLE-MESSAGE.DISABLED"))));
            return;
        } else {
            Main.getPlugin().getReport().getReportTogglePlayers().remove(player);
            player.sendMessage((BaseComponent)new TextComponent(ChatColor.translateAlternateColorCodes('&', Main.getPlugin().getConfig().getString("REPORT.TOGGLE-MESSAGE.ENABLED"))));
            return;
        }
    }
}

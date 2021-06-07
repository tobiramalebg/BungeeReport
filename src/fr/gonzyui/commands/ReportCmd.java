package fr.gonzyui.commands;

import fr.gonzyui.Main;
import fr.gonzyui.managers.CooldownManager;
import fr.gonzyui.managers.DurationFormatUtils;
import net.md_5.bungee.api.plugin.*;
import net.md_5.bungee.api.connection.*;
import net.md_5.bungee.api.chat.*;

import net.md_5.bungee.api.*;

public class ReportCmd extends Command {
    public ReportCmd() {
        super("report", "", new String[] { "breport" });
    }

    public void execute(final CommandSender sender, final String[] args) {
        final ProxiedPlayer player = (ProxiedPlayer)sender;
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', Main.getPlugin().getConfig().getString("OTHERS.NO-CONSOLE"))));
            return;
        }
        else if (!sender.hasPermission("breport.report")) {
            sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', Main.getPlugin().getConfig().getString("OTHERS.NO-PERMISSIONS"))));
            return;
        }
        else if(Main.getPlugin().getReport().getServersDisableReportCommand(player)) {
            sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', Main.getPlugin().getConfig().getString("OTHERS.NO-SERVER")).replace("<server>", player.getServer().getInfo().getName())));
            return;
        }
        else if (args.length == 0) {
            for (String usage : Main.getPlugin().getConfig().getStringList("REPORT.USAGE.INCORRECT")) {
                sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', usage)));
            }
            return;
        }
        else if (args.length == 1) {
            for (String noreason : Main.getPlugin().getConfig().getStringList("REPORT.USAGE.NO-REASON")) {
                sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', noreason).replace("<target>", args[0])));
            }
            return;
        }
        else if(!Main.getPlugin().getConfig().getBoolean("REPORT.REASONS.USE-CUSTOM-REASON") && !Main.getPlugin().getReport().getReportReasons(args[1])) {
            for(String reasons : Main.getPlugin().getConfig().getStringList("REPORT.USAGE.UNKNOWN-REASON")) {
                sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', reasons)));
            }
            return;
        }
        else {
            final ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
            if (target == player) {
                player.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', Main.getPlugin().getConfig().getString("OTHERS.NO-YOURSELF"))));
                return;
            }
            else if (target == null || !target.isConnected()) {
                player.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', Main.getPlugin().getConfig().getString("OTHERS.NO-ONLINE")).replace("<target>", args[0])));
                return;
            }
            else if(CooldownManager.isOnCooldown("cooldown", player)){
                player.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', Main.getPlugin().getConfig().getString("OTHERS.COOLDOWN")).replace("<cooldown>", DurationFormatUtils.formatDurationWords(CooldownManager.getCooldownForPlayerLong("cooldown", player), true, true))));
                return;
            } else {
                String message = "";
                for (int i = 1; i < args.length; ++i) {
                    message = String.valueOf(message) + args[i] + " ";
                }
                for (final ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
                    if (players.hasPermission("breport.notify") && !Main.getPlugin().getReport().getReportTogglePlayers().contains(players) && !Main.getPlugin().getReport().getServersDisableNotifyMessage(player)) {
                        Main.getPlugin().getReport().getReportMessage(players, player, target, message);
                        Main.getPlugin().getReport().getReportTitle(players, player, target);
                        Main.getPlugin().getReport().getReportedPlayer().add(target);
                    }
                }
                Main.getPlugin().getReport().getReportCooldown(player, target);
                return;
            }
        }
    }
}
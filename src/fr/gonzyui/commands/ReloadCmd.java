package fr.gonzyui.commands;

import fr.gonzyui.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class ReloadCmd extends Command {
    public ReloadCmd() {
        super("bungeereport", "", new String[] { "breport"});
    }

    public void execute(final CommandSender sender, final String[] args) {
        if (!(sender.hasPermission("breport.reload"))) {
            sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', Main.getPlugin().getConfig().getString("OTHERS.NO-PERMISSIONS"))));
            return;
        } else if (args.length < 1 || args.length > 1) {
            sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&cUsage: /bungeereport reload")));
            return;
        } else {
            if (args[0].equalsIgnoreCase("reload")) {
                Main.getPlugin().reloadConfig();
                sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&3BugeeReport&f config.yml has been &aupdated&f.")));
                return;
            } else {
                sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&cUsage: /bungeereport reload")));
                return;
            }
        }
    }
}

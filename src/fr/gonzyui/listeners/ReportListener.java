package fr.gonzyui.listeners;

import fr.gonzyui.Main;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ReportListener implements Listener {

    @EventHandler
    public void onTabComplete(final TabCompleteEvent e) {
        final String[] args = e.getCursor().toLowerCase().split(" ");
        if (args.length >= 1 && args[0].startsWith("/") && args.length > 1 && args[0].equalsIgnoreCase("/report") || args.length > 1 && args[0].equalsIgnoreCase("/breport")) {
            final String check = args[args.length - 1];
            for (final ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                if (player.getName().toLowerCase().startsWith(check)) {
                    e.getSuggestions().add(player.getName());
                }
            }
        }
    }

    @EventHandler
    public void onQuit(final PlayerDisconnectEvent e) {
        if (Main.getPlugin().getReport().getReportedPlayer().contains(e.getPlayer())) {
            Main.getPlugin().getReport().getReportedPlayer().remove(e.getPlayer());
        }
    }
}

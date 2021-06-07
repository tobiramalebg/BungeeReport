package fr.gonzyui;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;

import fr.gonzyui.commands.ReloadCmd;
import fr.gonzyui.commands.ReportCmd;
import fr.gonzyui.commands.ReportListCmd;
import fr.gonzyui.commands.ToggleReports;
import fr.gonzyui.listeners.ReportListener;
import fr.gonzyui.managers.CooldownManager;
import fr.gonzyui.managers.ReportManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class Main extends Plugin {
    private static Main plugin;

    private Configuration config;

    private ReportManager reportManager;

    public void onEnable(){
        plugin = this;
        loadConfig();
        reportManager = new ReportManager();
        CooldownManager.createCooldown("cooldown");
        getProxy().getPluginManager().registerCommand(this, new ReportCmd());
        getProxy().getPluginManager().registerCommand(this, new ReportListCmd());
        getProxy().getPluginManager().registerCommand(this, new ReloadCmd());
        getProxy().getPluginManager().registerCommand(this, new ToggleReports());
        getProxy().getPluginManager().registerListener(this, new ReportListener());
        ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("Successfully loaded BungeeReport."));
    }

    public void onDisable(){
        plugin = null;
    }

    public void loadConfig(){
        if(!this.getDataFolder().exists()) {
            this.getDataFolder().mkdir();
        }
        File file = new File(this.getDataFolder(), "config.yml");
        if(!file.exists()) {
            try(InputStream in = this.getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath(), new CopyOption[0]);
            } catch(IOException e){
                e.printStackTrace();
            }
        } try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(this.getDataFolder(), "config.yml"));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadConfig() {
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(Main.getPlugin().getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Main getPlugin() {
        return plugin;
    }

    public Configuration getConfig() {
        return config;
    }

    public ReportManager getReport() {
        return reportManager;
    }
}
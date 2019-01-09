package net.seliba.sbmanager;

/*
SchematicBrushManager created by Seliba
*/

import net.seliba.sbmanager.commands.BManagerCommand;
import net.seliba.sbmanager.commands.SManagerCommand;
import net.seliba.sbmanager.files.FileLoader;
import net.seliba.sbmanager.listener.InventoryClickListener;
import net.seliba.sbmanager.listener.ServerSwitchListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class SBManager extends JavaPlugin {

    private FileLoader fileLoader;

    @Override
    public void onEnable() {
        System.out.println("[SBManager] Gestartet!");

        fileLoader = new FileLoader();

        registerCommands();
        registerEvents();
    }

    @Override
    public void onDisable() {
        System.out.println("[SBManager] Gestoppt!");
    }

    private void registerCommands() {
        getCommand("sm").setExecutor(new SManagerCommand());
        getCommand("bm").setExecutor(new BManagerCommand());
    }

    private void registerEvents() {
        Bukkit.getServer().getPluginManager().registerEvents(new ServerSwitchListener(fileLoader), this);
        Bukkit.getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
    }

}

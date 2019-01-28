package net.seliba.sbmanager;

/*
SchematicBrushManager created by Seliba
*/

import net.seliba.sbmanager.commands.BManagerCommand;
import net.seliba.sbmanager.commands.SManagerCommand;
import net.seliba.sbmanager.files.BrushFile;
import net.seliba.sbmanager.files.FileLoader;
import net.seliba.sbmanager.listener.AsyncPlayerChatListener;
import net.seliba.sbmanager.listener.InventoryClickListener;
import net.seliba.sbmanager.listener.ServerSwitchListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SBManager extends JavaPlugin {

  private FileLoader fileLoader;
  private static BrushFile brushFile;

  @Override
  public void onEnable() {
    System.out.println("[SchematicBrushManager] Gestartet!");

    fileLoader = new FileLoader();
    brushFile = new BrushFile();

    registerCommands();
    registerEvents();
  }

  @Override
  public void onDisable() {
    System.out.println("[SchematicBrushManager] Gestoppt!");
  }

  private void registerCommands() {
    getCommand("sm").setExecutor(new SManagerCommand());
    getCommand("bm").setExecutor(new BManagerCommand());
  }

  private void registerEvents() {
    PluginManager pluginManager = Bukkit.getPluginManager();

    pluginManager.registerEvents(new ServerSwitchListener(fileLoader), this);
    pluginManager.registerEvents(new InventoryClickListener(fileLoader), this);
    pluginManager.registerEvents(new AsyncPlayerChatListener(), this);
  }

  public static BrushFile getBrushFile() {
    return brushFile;
  }

}

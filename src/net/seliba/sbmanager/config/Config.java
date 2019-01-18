package net.seliba.sbmanager.config;

/*
SchematicBrushManager created by Seliba
*/

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ListIterator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Config extends YamlConfiguration {

  private String uuid;
  private JavaPlugin javaPlugin;

  private File file;

  public Config(String uuid, JavaPlugin javaPlugin) {
    this.uuid = uuid;
    this.javaPlugin = javaPlugin;

    reload();
  }

  public void reload() {
    file = new File("../SBManager/brushes", uuid + ".yml");

    try {
      if (!file.exists()) {
        if (!file.createNewFile()) {
          throw new RuntimeException("Could not create Config " + uuid + ".yml");
        }
      }

      load(file);
    } catch (IOException | InvalidConfigurationException e) {
      e.printStackTrace();
    }
  }

  public void save() {
    try {
      save(file);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void setDefault(String path, Object value) {
    if (!isSet(path)) {
      set(path, value);
    }
  }

  public String getTranslatedString(String path, String def) {
    return ChatColor.translateAlternateColorCodes('&', getString(path, def));
  }

  public String getTranslatedString(String path) {
    return getTranslatedString(path, "null");
  }

  public List<String> getTranslatedStringList(String path) {
    List<String> stringList = getStringList(path);
    ListIterator<String> iterator = stringList.listIterator();

    while (iterator.hasNext()) {
      iterator.set(ChatColor.translateAlternateColorCodes('&', iterator.next()));
    }

    return stringList;
  }

  public void setLocation(String path, Location location) {
    set(path + ".world", location.getWorld().getName());
    set(path + ".x", location.getX());
    set(path + ".y", location.getY());
    set(path + ".z", location.getZ());
    set(path + ".yaw", location.getYaw());
    set(path + ".pitch", location.getPitch());
  }

  public Location getLocation(String path) {
    return getLocation(path, null);
  }

  public Location getLocation(String path, Location def) {
    if (!isSet(path + ".world")) {
      return def;
    }

    return new Location(
        Bukkit.getWorld(getString(path + ".world")),
        getDouble(path + ".x"),
        getDouble(path + ".y"),
        getDouble(path + ".z"),
        (float) getDouble(path + ".yaw"),
        (float) getDouble(path + ".pitch")
    );
  }

}
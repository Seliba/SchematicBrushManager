package net.seliba.sbmanager.schematics;

/*
SchematicBrushManager created by Seliba
*/

import java.util.HashMap;
import org.bukkit.entity.Player;

public class SchematicDataManager {

  private static HashMap<Player, String> schematicNames = new HashMap<>();
  private static HashMap<Player, String> schematicUrls = new HashMap<>();

  public static void addSchematicName(Player player, String name) {
    schematicNames.put(player, name);
  }

  public static boolean hasSchematicName(Player player) {
    return schematicNames.containsKey(player);
  }

  public static String getSchematicName(Player player) {
    return schematicNames.get(player);
  }

  public static void removeSchematicName(Player player) {
    if (hasSchematicName(player)) {
      schematicNames.remove(player);
    }
  }

  public static void addSchematicUrl(Player player, String name) {
    schematicUrls.put(player, name);
  }

  public static boolean hasSchematicUrl(Player player) {
    return schematicUrls.containsKey(player);
  }

  public static String getSchematicUrl(Player player) {
    return schematicUrls.get(player);
  }

  public static void removeSchematicUrl(Player player) {
    schematicUrls.remove(player);
  }

}

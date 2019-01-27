package net.seliba.sbmanager.brushes;

/*
SchematicBrushManager created by Seliba
*/

import java.util.HashMap;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BrushDataManager {

  private static HashMap<Player, ItemStack> brushItems = new HashMap<>();
  private static HashMap<Player, String> brushNames = new HashMap<>();

  public static void setBrushItem(Player player, ItemStack itemStack) {
    if (hasBrushItem(player)) {
      brushItems.remove(player);
    }

    brushItems.put(player, itemStack);
  }

  public static ItemStack getBrushItem(Player player) {
    return brushItems.get(player);
  }

  public static boolean hasBrushItem(Player player) {
    return brushItems.containsKey(player);
  }

  public static void removeBrushItem(Player player) {
    if (!hasBrushItem(player)) {
      return;
    }

    brushItems.remove(player);
  }

  public static void setBrushName(Player player, String name) {
    if (hasBrushName(player)) {
      brushNames.remove(player);
    }

    brushNames.put(player, name);
  }

  public static String getBrushName(Player player) {
    return brushNames.get(player);
  }

  public static boolean hasBrushName(Player player) {
    return brushNames.containsKey(player);
  }

  public static void removeBrushName(Player player) {
    if (!brushNames.containsKey(player)) {
      return;
    }

    brushNames.remove(player);
  }

}

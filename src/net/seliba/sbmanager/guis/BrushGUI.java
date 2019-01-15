package net.seliba.sbmanager.guis;

/*
SchematicBrushManager created by Seliba
*/

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class BrushGUI {

  private static int INVENTORY_SIZE = 6 * 9;

  public static void open(Player player) {
    Inventory inventory = Bukkit.createInventory(null, INVENTORY_SIZE, "");
  }

}

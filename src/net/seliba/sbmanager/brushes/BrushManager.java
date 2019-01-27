package net.seliba.sbmanager.brushes;

/*
SchematicBrushManager created by Seliba
*/

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BrushManager {

  public static void createBrush(Player player) {
    ItemStack brushItem = BrushDataManager.getBrushItem(player);
    String brushName = BrushDataManager.getBrushName(player);

    //TODO
  }

}

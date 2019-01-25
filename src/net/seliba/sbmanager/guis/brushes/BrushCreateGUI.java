package net.seliba.sbmanager.guis.brushes;

/*
SchematicBrushManager created by Seliba
*/

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BrushCreateGUI {

  private static int INVENTORY_SIZE = 3 * 9;
  private static int ITEM_SLOT = 13;

  public static void open(Player player) {

    Inventory inventory = Bukkit
        .createInventory(null, INVENTORY_SIZE, "§aBrushes §7| §aErstellung");

    ItemStack schematicItem = player.getInventory().getItemInMainHand();
    ItemMeta schematicItemMeta = schematicItem.getItemMeta();

    List<String> lore = schematicItemMeta.getLore();
    lore.add(" ");
    lore.add("§aErstelle einen neuen Brush!");
    schematicItemMeta.setLore(lore);
    schematicItem.setItemMeta(schematicItemMeta);

    inventory.setItem(ITEM_SLOT, schematicItem);

    player.openInventory(inventory);
  }

}

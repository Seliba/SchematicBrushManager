package net.seliba.sbmanager.guis.brushes;

/*
SchematicBrushManager created by Seliba
*/

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class BrushSendGUI {

  public static void open(Player player, String schematicName) {
    Inventory inventory = Bukkit
        .createInventory(null, 6 * 9, "§a" + schematicName + " §7| §aBrushes senden");

    for(Player onlinePlayer : Bukkit.getServer().getOnlinePlayers()) {
      ItemStack skullItem = new ItemStack(Material.PLAYER_HEAD);
      SkullMeta itemMeta = (SkullMeta) skullItem.getItemMeta();

      itemMeta.setDisplayName("§a" + onlinePlayer.getName());
      itemMeta.setOwningPlayer(onlinePlayer);

      skullItem.setItemMeta(itemMeta);
      inventory.addItem(skullItem);
    }

    player.openInventory(inventory);
  }

}

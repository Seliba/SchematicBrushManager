package net.seliba.sbmanager.guis.brushes;

/*
SchematicBrushManager created by Seliba
*/

import net.seliba.sbmanager.SBManager;
import net.seliba.sbmanager.config.Config;
import net.seliba.sbmanager.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class BrushGUI {

  private static int INVENTORY_SIZE = 6 * 9;
  private static final String[] LORE = new String[]{"§6Verwende den Brush mit Linksklick",
      "§6Verwalte den Brush mit Rechtsklick"};

  public static void open(Player player) {
    Inventory inventory = Bukkit.createInventory(null, INVENTORY_SIZE, "§aBrushes");

    String uuid = player.getUniqueId().toString();
    Config playerBrushData = new Config(uuid, SBManager.getProvidingPlugin(SBManager.class));

    for (int i = 0; i < playerBrushData.getStringList("brushes-list").size(); i++) {
      String name = playerBrushData.getString("brushes." + i + ".name");
      Material material = Material.valueOf(playerBrushData.getString("brushes." + i + ".material"));
      String[] lore = new String[]{
          "§6Command: §a" + playerBrushData.getString("brushes." + i + ".command"),
          "§6Verwende den Brush mit Linksklick",
          "§6Verwalte den Brush mit Rechtsklick"
      };
      inventory.setItem(i, new ItemBuilder(material).setName(name).setLore(lore).build());
    }

    player.openInventory(inventory);
  }

}

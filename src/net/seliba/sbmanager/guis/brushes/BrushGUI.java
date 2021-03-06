package net.seliba.sbmanager.guis.brushes;

/*
SchematicBrushManager created by Seliba
*/

import net.seliba.sbmanager.SBManager;
import net.seliba.sbmanager.files.BrushFile;
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
    BrushFile brushData = SBManager.getBrushFile();

    for (int i = 0; i < brushData.getStringList(uuid + ".brushes-list").size(); i++) {
      String name = "§a" + brushData.getString(uuid + ".brushes." + i + ".name");
      Material material = Material.valueOf(brushData.getString(uuid + ".brushes." + i + ".material"));
      String[] lore = new String[] {
          "§6Command: §a/br " + brushData.getString(uuid + ".brushes." + i + ".command"),
          " ",
          "§6Verwende den Brush mit Linksklick",
          "§6Verwalte den Brush mit Rechtsklick"
      };
      inventory.setItem(i, new ItemBuilder(material).setName(name).setLore(lore).build());
    }

    player.openInventory(inventory);
  }

}

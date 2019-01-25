package net.seliba.sbmanager.guis.schematics;

/*
SchematicBrushManager created by Seliba
*/

import java.io.File;
import net.seliba.sbmanager.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class SchematicGUI {

  private static final int INVENTORY_SIZE = 6 * 9;
  private static final String[] LORE = new String[]{"§6Verwende das Schematic mit Linksklick",
      "§6Verwalte das Schematic mit Rechtsklick"};

  public static void open(Player player) {
    Inventory inventory = Bukkit.createInventory(null, INVENTORY_SIZE, "§aSchematics");

    String uuid = player.getUniqueId().toString();
    File playerDirectory = new File("plugins/FastAsyncWorldEdit/schematics/" + uuid + "/");
    File[] playerSchematics = playerDirectory.listFiles();

    //To fix
    if (playerSchematics.length > INVENTORY_SIZE) {
      return;
    }

    for (int i = 0; i < playerSchematics.length; i++) {
      File schematicFile = playerSchematics[i];
      if (schematicFile.getName().endsWith(".schem") || schematicFile.getName()
          .endsWith(".schematic")) {
        inventory.addItem(new ItemBuilder(Material.PAPER).setName(
            "§a" + schematicFile.getName().replaceAll(".schematic", "").replaceAll(".schem", ""))
            .setLore(LORE).build());
      }
    }

    player.openInventory(inventory);
  }

}

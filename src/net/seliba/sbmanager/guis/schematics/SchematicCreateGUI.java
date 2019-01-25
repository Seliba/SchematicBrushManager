package net.seliba.sbmanager.guis.schematics;

/*
SchematicBrushManager created by Seliba
*/

import net.seliba.sbmanager.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SchematicCreateGUI {

  private final static int INVENTORY_SIZE = 3 * 9, SELECTION_SLOT = 11, DOWNLOAD_SLOT = 15;

  public static void open(Player player) {
    Inventory inventory = Bukkit
        .createInventory(null, INVENTORY_SIZE, "§aSchematics §7| §aErstellung");

    ItemStack download = new ItemBuilder(Material.PAPER).setName("§aDownload").setLore(new String[]{
        "§aDownloade ein Schematic von einer Webseite\n§6Dazu benötigst du den Link zu dem Schematic!"})
        .build();
    ItemStack selection = new ItemBuilder(Material.WOODEN_AXE).setName("§aAuswahl").setLore(
        new String[]{
            "§aErstelle ein neues Schematic aus deinen Bauwerken\n§6Dazu musst du dein Bauwerk mit //copy kopieren!"})
        .build();

    inventory.setItem(SELECTION_SLOT, selection);
    inventory.setItem(DOWNLOAD_SLOT, download);

    player.openInventory(inventory);
  }

}

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

public class ConfirmationGUI {

  private static final int INVENTORY_SIZE =
      3 * 9, ACCEPT_SLOT = 11, DECLINE_SLOT = 15, QUESTION_SLOT = 13;
  private static final String[] ACCEPT_LORE = new String[]{"§aLöscht das Schematic",
      "§c§lAchtung, nach der Löschung ist eine", "§c§lWiederherstellung nicht mehr möglich!"};
  private static final String[] DECLINE_LORE = new String[]{"§aBricht den Vorgang ab"};
  private static final String[] QUESTION_LORE = new String[]{"§6Bist du dir sicher, dass du ",
      "§6das Schematic §cunwiederruflich §6löschen möchtest?"};

  public static void open(Player player, String schematicName) {
    Inventory inventory = Bukkit
        .createInventory(null, INVENTORY_SIZE, "§a" + schematicName + " §7| §aBestätigung");

    ItemStack accept = new ItemBuilder(Material.EMERALD_BLOCK).setName("§aBestätigen")
        .setLore(ACCEPT_LORE).build();
    ItemStack decline = new ItemBuilder(Material.REDSTONE_BLOCK).setName("§cAbbrechen")
        .setLore(DECLINE_LORE).build();
    ItemStack question = new ItemBuilder(Material.PAPER).setName("§aBestätigung")
        .setLore(QUESTION_LORE).build();

    inventory.setItem(ACCEPT_SLOT, accept);
    inventory.setItem(DECLINE_SLOT, decline);
    inventory.setItem(QUESTION_SLOT, question);

    player.openInventory(inventory);
  }

}

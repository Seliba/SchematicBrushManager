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

public class SchematicManagerGUI {

    private static final int INVENTORY_SIZE = 3 * 9, SEND_SLOT = 11, DELETE_SLOT = 15;
    private static final String[] SEND_LORE = new String[]{"§6Sende das Schematic an andere Spieler"}, DELETE_LORE = new String[]{"§6Lösche das Schematic"};

    public static void open(Player player, String schematicName) {
        Inventory inventory = Bukkit.createInventory(null, INVENTORY_SIZE, "§a" + schematicName + " §7| §aVerwaltung");

        ItemStack sendItem = new ItemBuilder(Material.ARROW).setName("§aSenden").setLore(SEND_LORE).build();
        ItemStack deleteItem = new ItemBuilder(Material.REDSTONE_BLOCK).setName("§cLöschen").setLore(DELETE_LORE).build();

        inventory.setItem(SEND_SLOT, sendItem);
        inventory.setItem(DELETE_SLOT, deleteItem);

        player.openInventory(inventory);
    }

}

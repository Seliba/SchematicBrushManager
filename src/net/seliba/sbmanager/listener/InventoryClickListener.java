package net.seliba.sbmanager.listener;

/*
SchematicBrushManager created by Seliba
*/

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.seliba.sbmanager.guis.ConfirmationGUI;
import net.seliba.sbmanager.guis.SchematicManagerGUI;
import net.seliba.sbmanager.guis.SendGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.io.File;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(event.getClickedInventory() == null || event.getCurrentItem() == null) return;
        Player player = (Player) event.getWhoClicked();
        if(event.getClickedInventory().getName().equals("§aSchematics")) {
            event.setCancelled(true);
            String schematicName = event.getCurrentItem().getItemMeta().getDisplayName().replaceAll("§a", "");
            if(isSchem(player, schematicName)) {
                schematicName = schematicName + ".schem";
            } else {
                schematicName = schematicName + ".schematic";
            }
            if(player.hasPermission("sbmanager.schematics")) {
                if(event.isLeftClick()) {
                    player.closeInventory();
                    player.performCommand("schem load " + schematicName);
                } else {
                    player.closeInventory();
                    SchematicManagerGUI.open(player, schematicName.replaceAll(".schematic", "").replaceAll(".schem", ""));
                }
            }
        } else if(event.getClickedInventory().getName().endsWith(" §7| §aVerwaltung")) {
            event.setCancelled(true);
            String schematicName = getSchematicName(event.getClickedInventory().getName(), player);
            if(player.hasPermission("sbmanager.schematics")) {
                if(event.getCurrentItem().getType() == Material.ARROW) {
                    SendGUI.open(player, schematicName.replaceAll(".schematic", "").replaceAll(".schem", ""));
                } else if(event.getCurrentItem().getType() == Material.REDSTONE_BLOCK) {
                    ConfirmationGUI.open(player, schematicName.replaceAll(".schematic", "").replaceAll(".schem", ""));
                }
            }
        } else if(event.getClickedInventory().getName().endsWith("§7| §aBestätigung")) {
            event.setCancelled(true);
            String schematicName = getSchematicName(event.getClickedInventory().getName(), player);
            if(player.hasPermission("sbmanager.schematics")) {
                if(event.getCurrentItem().getType() == Material.EMERALD_BLOCK) {
                    player.closeInventory();
                    player.sendMessage("§aDu hast das Schematic erfolgreich gelöscht!");
                    deleteFiles(schematicName, player);
                } else if(event.getCurrentItem().getType() == Material.REDSTONE_BLOCK) {
                    player.closeInventory();
                    player.sendMessage("§aDu hast den Vorgang erfolgreich abgebrochen!");
                }
            }
        } else if(event.getClickedInventory().getName().endsWith(" §7| §aSenden")) {
            event.setCancelled(true);
            String schematicName = getSchematicName(event.getClickedInventory().getName(), player);
            if(player.hasPermission("sbmanager.schematics")) {
                if(event.getCurrentItem().getType() != Material.PLAYER_HEAD) return;
                Player receiver = Bukkit.getPlayer(event.getCurrentItem().getItemMeta().getDisplayName().replaceAll("§a", ""));
                if(receiver == null) {
                    player.closeInventory();
                    player.sendMessage("§cDieser Spieler ist offline!");
                    return;
                }
                TextComponent message = new TextComponent();
                TextComponent accept = new TextComponent();
                TextComponent warning = new TextComponent();
                message.setText("§6" + player.getName() + " §amöchte dir das Schematic §6" + schematicName.replaceAll(".schematic", "").replaceAll(".schem", "") + " §aschicken!");
                accept.setText("§a§lKlicke hier zum Annehmen!");
                warning.setText("§c§lAchtung, dieser Vorgang überschreibt andere Dateien mit dem Namen!");
                accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sm accept " + player.getUniqueId() + " " + schematicName));
                accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aAnnehmen").create()));
                receiver.spigot().sendMessage(message);
                receiver.spigot().sendMessage(accept);
                receiver.spigot().sendMessage(warning);
                player.closeInventory();
                player.sendMessage("§aDu hast §6" + receiver.getName() + " §aerfolgreich das Schematic gesendet!");
            }
        } else if(event.getClickedInventory().getName().equals("§aSchematics §7| §aErstellung")) {

        }
    }

    private boolean isSchem(Player player, String name) {
        File file = new File("plugins/FastAsyncWorldEdit/schematics/" + player.getUniqueId().toString() + "/" + name + ".schem");
        return file.exists();
    }

    private String getSchematicName(String name, Player player) {
        String schematicName = name.replaceAll("§a", "").split(" ")[0];
        if(isSchem(player, schematicName)) {
            schematicName = schematicName + ".schem";
        } else {
            schematicName = schematicName + ".schematic";
        }
        return schematicName;
    }

    private void deleteFiles(String schematicName, Player player) {
        File localFile = new File("plugins/FastAsyncWorldEdit/schematics/" + player.getUniqueId().toString() + "/" + schematicName);
        File publicFile = new File("../SBManager/schematics/" + player.getUniqueId().toString() + "/" + schematicName);
        if(localFile.exists()) localFile.delete();
        if(publicFile.exists()) publicFile.delete();
    }

}

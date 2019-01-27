package net.seliba.sbmanager.listener;

/*
SchematicBrushManager created by Seliba
*/

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.seliba.sbmanager.brushes.BrushDataManager;
import net.seliba.sbmanager.files.FileLoader;
import net.seliba.sbmanager.guis.schematics.ConfirmationGUI;
import net.seliba.sbmanager.guis.schematics.SchematicManagerGUI;
import net.seliba.sbmanager.guis.schematics.SendGUI;
import net.seliba.sbmanager.utils.AnswerManager;
import net.seliba.sbmanager.utils.AnswerManager.AnswerType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

  private FileLoader fileLoader;

  public InventoryClickListener(FileLoader fileLoader) {
    this.fileLoader = fileLoader;
  }

  @EventHandler
  public void onInventoryClick(InventoryClickEvent event) {
    if (event.getClickedInventory() == null || event.getCurrentItem() == null) {
      return;
    }
    Player player = (Player) event.getWhoClicked();
    if (event.getClickedInventory().getName().equals("§aSchematics")) {
      event.setCancelled(true);
      String schematicName = event.getCurrentItem().getItemMeta().getDisplayName()
          .replaceAll("§a", "");
      if (fileLoader.isSchem(player, schematicName)) {
        schematicName = schematicName + ".schem";
      } else {
        schematicName = schematicName + ".schematic";
      }
      if (player.hasPermission("sbmanager.schematics")) {
        if (event.isLeftClick()) {
          player.closeInventory();
          player.performCommand("schem load " + schematicName);
        } else {
          player.closeInventory();
          SchematicManagerGUI
              .open(player, schematicName.replaceAll(".schematic", "").replaceAll(".schem", ""));
        }
      }
    } else if (event.getClickedInventory().getName().endsWith(" §7| §aVerwaltung")) {
      event.setCancelled(true);
      String schematicName = fileLoader
          .getSchematicName(event.getClickedInventory().getName(), player);
      if (player.hasPermission("sbmanager.schematics")) {
        if (event.getCurrentItem().getType() == Material.ARROW) {
          SendGUI.open(player, schematicName.replaceAll(".schematic", "").replaceAll(".schem", ""));
        } else if (event.getCurrentItem().getType() == Material.REDSTONE_BLOCK) {
          ConfirmationGUI
              .open(player, schematicName.replaceAll(".schematic", "").replaceAll(".schem", ""));
        }
      }
    } else if (event.getClickedInventory().getName().endsWith("§7| §aBestätigung")) {
      event.setCancelled(true);
      String schematicName = fileLoader
          .getSchematicName(event.getClickedInventory().getName(), player);
      if (player.hasPermission("sbmanager.schematics")) {
        if (event.getCurrentItem().getType() == Material.EMERALD_BLOCK) {
          player.closeInventory();
          player.sendMessage("§aDu hast das Schematic erfolgreich gelöscht!");
          fileLoader.deleteFiles(schematicName, player);
        } else if (event.getCurrentItem().getType() == Material.REDSTONE_BLOCK) {
          player.closeInventory();
          player.sendMessage("§aDu hast den Vorgang erfolgreich abgebrochen!");
        }
      }
    } else if (event.getClickedInventory().getName().endsWith(" §7| §aSenden")) {
      event.setCancelled(true);
      String schematicName = fileLoader
          .getSchematicName(event.getClickedInventory().getName(), player);
      if (player.hasPermission("sbmanager.schematics")) {
        if (event.getCurrentItem().getType() != Material.PLAYER_HEAD) {
          return;
        }
        Player receiver = Bukkit
            .getPlayer(event.getCurrentItem().getItemMeta().getDisplayName().replaceAll("§a", ""));
        if (receiver == null) {
          player.closeInventory();
          player.sendMessage("§cDieser Spieler ist offline!");
          return;
        }
        TextComponent message = new TextComponent();
        TextComponent accept = new TextComponent();
        TextComponent warning = new TextComponent();
        message.setText("§6" + player.getName() + " §amöchte dir das Schematic §6" + schematicName
            .replaceAll(".schematic", "").replaceAll(".schem", "") + " §aschicken!");
        accept.setText("§a§lKlicke hier zum Annehmen!");
        warning.setText("§c§lAchtung, dieser Vorgang überschreibt andere Dateien mit dem Namen!");
        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
            "/sm accept " + player.getUniqueId() + " " + schematicName));
        accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
            new ComponentBuilder("§aAnnehmen").create()));
        receiver.spigot().sendMessage(message);
        receiver.spigot().sendMessage(accept);
        receiver.spigot().sendMessage(warning);
        player.closeInventory();
        player.sendMessage(
            "§aDu hast §6" + receiver.getName() + " §aerfolgreich das Schematic gesendet!");
      }
    } else if (event.getClickedInventory().getName().equals("§aSchematics §7| §aErstellung")) {
      event.setCancelled(true);
      if (player.hasPermission("sbmanager.schematics")) {
        if (event.getCurrentItem().getType() == Material.PAPER) {
          player.closeInventory();
          AnswerManager.addRequest(player, AnswerType.SCHEMATIC_WEB_WEBSITE);
          player.sendMessage("§aBitte gebe die URL zu der Webseite ein!");
        } else if (event.getCurrentItem().getType() == Material.WOODEN_AXE) {
          player.closeInventory();
          AnswerManager.addRequest(player, AnswerType.SCHEMATIC_CUSTOM_NAME);
          player.sendMessage("§aBitte gebe den Namen des Schematics ein!");
        }
      }
    } else if (event.getClickedInventory().getName().equals("§aBrushes §7| §aErstellung")) {
      event.setCancelled(true);
      if (player.hasPermission("sbmanager.brushes")) {
        if (event.getSlot() == 13) {
          player.closeInventory();
          BrushDataManager.setBrushItem(player, event.getCurrentItem());
          AnswerManager.addRequest(player, AnswerType.BRUSH_NAME);
          player.sendMessage("§aBitte gebe den Namen des Brushes ein!");
        }
      }
    }
  }

}

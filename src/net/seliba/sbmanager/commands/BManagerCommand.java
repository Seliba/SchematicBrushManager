package net.seliba.sbmanager.commands;

/*
SchematicBrushManager created by Seliba
*/

import net.seliba.sbmanager.guis.brushes.BrushCreateGUI;
import net.seliba.sbmanager.guis.brushes.BrushGUI;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BManagerCommand implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
    if (sender instanceof Player) {
      Player player = (Player) sender;
      if (player.hasPermission("sbmanager.brushes")) {
        if (args.length != 1) {
          BrushGUI.open(player);
          return true;
        }
        if (args[0].equalsIgnoreCase("create")) {
          if(player.getInventory().getItemInMainHand().getType() == Material.AIR) {
            player.sendMessage("§cBitte halte ein Item in der Hand!");
            return true;
          }
          if(!player.getInventory().getItemInMainHand().getItemMeta().hasLore() || !player.getInventory().getItemInMainHand().getItemMeta().getLore().get(0).startsWith("{")) {
            player.sendMessage("§cBitte wähle ein Brush-Item!");
            return true;
          }
          BrushCreateGUI.open(player);
          return true;
        }
      } else {
        player.sendMessage("§cDazu hast du keine Rechte!");
      }
    } else {
      sender.sendMessage("§cDazu musst du ein Spieler sein!");
    }
    return true;
  }

}

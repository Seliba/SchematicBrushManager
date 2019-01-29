package net.seliba.sbmanager.commands;

/*
SchematicBrushManager created by Seliba
*/

import java.util.List;
import net.seliba.sbmanager.SBManager;
import net.seliba.sbmanager.files.BrushFile;
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
        if (args.length != 1 && args.length != 3) {
          BrushGUI.open(player);
          return true;
        }
        if (args[0].equalsIgnoreCase("create") && args.length == 1) {
          if (player.getInventory().getItemInMainHand().getType() == Material.AIR) {
            player.sendMessage("§cBitte halte ein Item in der Hand!");
            return true;
          }
          if (!player.getInventory().getItemInMainHand().getItemMeta().hasLore() || !player
              .getInventory().getItemInMainHand().getItemMeta().getLore().get(0).startsWith("{")) {
            player.sendMessage("§cBitte wähle ein Brush-Item!");
            return true;
          }
          BrushCreateGUI.open(player);
          return true;
        } else if (args[0].equalsIgnoreCase("accept") && args.length == 3) {
          BrushFile brushFile = SBManager.getBrushFile();
          String brushName = args[2];

          String fromUuid = args[1];
          int fromBrushId = brushFile.getStringList(fromUuid + ".brushes-list").indexOf(brushName);
          List<String> fromPlayerBrushes = brushFile.getStringList(fromUuid + ".brushes-list");

          String toUuid = player.getUniqueId().toString();
          int toBrushId = brushFile.getStringList(toUuid + ".brushes-list").size();
          List<String> toPlayerBrushes = brushFile.getStringList(toUuid + ".brushes-list");

          toPlayerBrushes.add(brushName);

          brushFile.set(toUuid + ".brushes." + toBrushId + ".name", brushFile.getString(fromUuid + ".brushes." + fromBrushId + ".name"));
          brushFile.set(toUuid + ".brushes." + toBrushId + ".command", brushFile.getString(fromUuid + ".brushes." + fromBrushId + ".command"));
          brushFile.set(toUuid + ".brushes." + toBrushId + ".material", brushFile.getString(fromUuid + ".brushes." + fromBrushId + ".material"));
          brushFile.set(toUuid + ".brushes-list", toPlayerBrushes);
          brushFile.save();

          player.sendMessage("§aDu hast den Brush erfolgreich erhalten!");
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

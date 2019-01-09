package net.seliba.sbmanager.commands;

/*
SchematicBrushManager created by Seliba
*/

import net.seliba.sbmanager.guis.BrushGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BManagerCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(player.hasPermission("sbmanager.brushes")) {
                BrushGUI.open(player);
            } else {
                player.sendMessage("§cDazu hast du keine Rechte!");
            }
        } else {
            sender.sendMessage("§cDazu musst du ein Spieler sein!");
        }
        return true;
    }

}

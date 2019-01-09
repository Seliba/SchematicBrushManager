package net.seliba.sbmanager.commands;

/*
SchematicBrushManager created by Seliba
*/

import net.seliba.sbmanager.guis.SchematicGUI;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class SManagerCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(player.hasPermission("sbmanager.schematics")) {
                if(args.length != 3 && args.length != 1) {
                    SchematicGUI.open(player);
                    return true;
                }
                if(args[0].equalsIgnoreCase("create")) {

                    return true;
                }
                File schematicFile = new File("../SBManager/schematics/" + args[1] + "/" + args[2]);
                if(!schematicFile.exists()) {
                    player.sendMessage("§cDieses Schematic existiert nicht!");
                    return true;
                }
                File localDirectory = new File("plugins/FastAsyncWorldEdit/schematics/" + player.getUniqueId().toString() + "/");
                File publicDirectory = new File("../SBManager/schematics/" + player.getUniqueId().toString() + "/");
                try {
                    FileUtils.copyFileToDirectory(schematicFile, localDirectory);
                    FileUtils.copyFileToDirectory(schematicFile, publicDirectory);
                } catch(IOException ex) {
                    Bukkit.getLogger().log(Level.WARNING, player.getName() + " konnte ein Schematic nicht empfangen. " +
                            "Bitte kontaktiere den Developer per Discord: https://discord.gg/nfQA2cZ");
                }
                player.sendMessage("§aDu hast das Schematic erfolgreich erhalten!");
            } else {
                player.sendMessage("§cDazu hast du keine Rechte!");
            }
        } else {
            sender.sendMessage("§cDazu musst du ein Spieler sein!");
        }
        return true;
    }

}

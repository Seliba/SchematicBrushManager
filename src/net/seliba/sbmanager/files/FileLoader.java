package net.seliba.sbmanager.files;

/*
SchematicBrushManager created by Seliba
*/

import java.io.File;
import java.io.IOException;
import java.util.List;
import net.seliba.sbmanager.SBManager;
import org.apache.commons.io.FileUtils;
import org.bukkit.entity.Player;

public class FileLoader {

    public void loadData(Player player) throws IOException {
        String uuid = player.getUniqueId().toString();
        File inputFile = new File("../SBManager/schematics/" + uuid + "/");
        File outputFile = new File("plugins/FastAsyncWorldEdit/schematics/" + uuid + "/");
        if(!outputFile.exists()) outputFile.mkdirs();
        if(inputFile.exists()) {
            FileUtils.copyDirectory(inputFile, outputFile);
        }
    }

    public void saveData(Player player) throws IOException {
        String uuid = player.getUniqueId().toString();
        File inputFile = new File("plugins/FastAsyncWorldEdit/schematics/" + uuid + "/");
        File outputFile = new File("../SBManager/schematics/" + uuid + "/");
        if(!outputFile.exists()) outputFile.mkdirs();
        if(inputFile.exists()) {
            FileUtils.copyDirectory(inputFile, outputFile);
            FileUtils.deleteDirectory(inputFile);
        }
    }

    public boolean isSchem(Player player, String name) {
        File file = new File(
            "plugins/FastAsyncWorldEdit/schematics/" + player.getUniqueId().toString() + "/" + name
                + ".schem");
        return file.exists();
    }

    public String getSchematicName(String name, Player player) {
        String schematicName = name.replaceAll("§a", "").split(" ")[0];
        if (isSchem(player, schematicName)) {
            schematicName = schematicName + ".schem";
        } else {
            schematicName = schematicName + ".schematic";
        }
        return schematicName;
    }

    public void deleteFiles(String schematicName, Player player) {
        File localFile = new File(
            "plugins/FastAsyncWorldEdit/schematics/" + player.getUniqueId().toString() + "/"
                + schematicName);
        File publicFile = new File(
            "../SBManager/schematics/" + player.getUniqueId().toString() + "/" + schematicName);
        if (localFile.exists()) {
            localFile.delete();
        }
        if (publicFile.exists()) {
            publicFile.delete();
        }
    }

    public void deleteBrush(String uuid, String brushName) {
        BrushFile brushFile = SBManager.getBrushFile();
        int brushId = brushFile.getStringList(uuid + ".brushes-list").indexOf(brushName);
        List<String> playerBrushes = brushFile.getStringList(uuid + ".brushes-list");

        for (int i = brushId; i < playerBrushes.size(); i++) {
            System.out.println(i);
            System.out.println(brushId);
            System.out.println(brushFile.getString(uuid + ".brushes." + (i + 1) + ".name"));
            brushFile.set(uuid + ".brushes." + i + ".name", brushFile.getString(uuid + ".brushes." + (i + 1) + ".name"));
            brushFile.set(uuid + ".brushes." + i + ".command", brushFile.getString(uuid + ".brushes." + (i + 1) + ".command"));
            brushFile.set(uuid + ".brushes." + i + ".material", brushFile.getString(uuid + ".brushes." + (i + 1) + ".material"));
        }

        playerBrushes.remove(brushName);
        brushFile.set(uuid + ".brushes-list", playerBrushes);
        brushFile.save();
    }

}

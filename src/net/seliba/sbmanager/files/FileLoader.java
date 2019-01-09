package net.seliba.sbmanager.files;

/*
SchematicBrushManager created by Seliba
*/

import org.apache.commons.io.FileUtils;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

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

}

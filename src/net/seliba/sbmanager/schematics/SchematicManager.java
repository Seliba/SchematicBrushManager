package net.seliba.sbmanager.schematics;

/*
SchematicBrushManager created by Seliba
*/

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import net.seliba.sbmanager.SBManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SchematicManager {

  public static void createWebSchematic(Player player) {
    if (!SchematicDataManager.hasSchematicName(player) || !SchematicDataManager
        .hasSchematicUrl(player)) {
      System.out.println(11);
      return;
    }
    downloadSchematic(player, SchematicDataManager.getSchematicUrl(player),
        SchematicDataManager.getSchematicName(player));
    SchematicDataManager.removeSchematicName(player);
    SchematicDataManager.removeSchematicUrl(player);
    System.out.println(12);
    player.sendMessage("§aDu hast das Schematic erfolgreich heruntergeladen!");
  }

  public static void createCustomSchematic(Player player) {
    if (!SchematicDataManager.hasSchematicName(player)) {
      return;
    }
    Bukkit.getScheduler().runTask(SBManager.getProvidingPlugin(SBManager.class),
        () -> {
          player.performCommand("/copy");
          player.performCommand("schem save " + SchematicDataManager.getSchematicName(player));
        });
    SchematicDataManager.removeSchematicName(player);
  }

  private static void downloadSchematic(Player player, String url, String name) {
    System.out.println(url + " " + name);
    File file = new File(
        "plugins/FastAsyncWorldEdit/schematics/" + player.getUniqueId().toString() + "/",
        name + (url.endsWith(".schem") ? ".schem" : ".schematic"));
    if (file.exists()) {
      return;
    }

    try {
      URL downloadUrl = new URL(url);
      HttpURLConnection connection = (HttpURLConnection) downloadUrl.openConnection();
      connection.setRequestMethod("GET");
      connection.addRequestProperty("User-Agent",
          "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:56.0) Gecko/20100101 Firefox/56.0");

      InputStream in = connection.getInputStream();
      OutputStream out = new BufferedOutputStream(new FileOutputStream(file));

      byte[] buffer = new byte[1024];

      int numRead;
      while ((numRead = in.read(buffer)) != -1) {
        out.write(buffer, 0, numRead);
      }

      in.close();
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}

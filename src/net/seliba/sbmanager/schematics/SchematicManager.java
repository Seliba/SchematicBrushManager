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
import org.bukkit.entity.Player;

public class SchematicManager {

  public static void createWebSchematic(Player player) {
    if (!SchematicDataManager.hasSchematicName(player) || !SchematicDataManager
        .hasSchematicUrl(player)) {
      return;
    }
    downloadSchematic(player, SchematicDataManager.getSchematicUrl(player),
        SchematicDataManager.getSchematicName(player));
    SchematicDataManager.removeSchematicName(player);
    SchematicDataManager.removeSchematicUrl(player);
  }

  public static void createCustomSchematic(Player player) {
    if (!SchematicDataManager.hasSchematicName(player)) {
      return;
    }
    player.performCommand("/copy");
    player.performCommand("schem save " + SchematicDataManager.getSchematicName(player));
    SchematicDataManager.removeSchematicName(player);
  }

  private static void downloadSchematic(Player player, String url, String name) {
    File file = new File("plugins/FastAsyncWorldEdit/schematics/", name);
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

package net.seliba.sbmanager.listener;

/*
SchematicBrushManager created by Seliba
*/

import net.seliba.sbmanager.schematics.AnswerManager;
import net.seliba.sbmanager.schematics.AnswerManager.AnswerType;
import net.seliba.sbmanager.schematics.SchematicDataManager;
import net.seliba.sbmanager.schematics.SchematicManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;

public class AsyncPlayerChatListener implements Listener {

  @EventHandler
  public void onChat(PlayerChatEvent event) {
    Player player = event.getPlayer();

    if(!AnswerManager.hasRequest(player)) {
      return;
    }

    switch(AnswerManager.getRequestType(player)) {
      case CREATION_CUSTOM_NAME:
        System.out.println("1");
        AnswerManager.removeRequest(player);
        SchematicDataManager.addSchematicName(player, event.getMessage());
        SchematicManager.createCustomSchematic(player);
        break;
      case CREATION_WEB_NAME:
        System.out.println("2");
        AnswerManager.removeRequest(player);
        SchematicDataManager.addSchematicName(player, event.getMessage());
        SchematicManager.createWebSchematic(player);
        break;
      case CREATION_WEB_WEBSITE:
        System.out.println("3");
        AnswerManager.removeRequest(player);
        SchematicDataManager.addSchematicUrl(player, event.getMessage());
        AnswerManager.addRequest(player, AnswerType.CREATION_WEB_NAME);
        player.sendMessage("§aBitte gebe den Namen des Schematics ein!");
        break;
    }
  }

}

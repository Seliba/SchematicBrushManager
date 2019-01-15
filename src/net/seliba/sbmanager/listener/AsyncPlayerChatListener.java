package net.seliba.sbmanager.listener;

/*
SchematicBrushManager created by Seliba
*/

import net.seliba.sbmanager.utils.AnswerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

//TODO

public class AsyncPlayerChatListener implements Listener {

  @EventHandler
  public void onAsyncChat(AsyncPlayerChatEvent event) {
    Player player = event.getPlayer();

    if(!AnswerManager.hasRequest(player)) {
      return;
    }

    switch(AnswerManager.getRequestType(player)) {
      case CREATION_CUSTOM_NAME:

        break;
      case CREATION_WEB_NAME:

        break;
      case CREATION_WEB_WEBSITE:

        break;
    }
  }

}

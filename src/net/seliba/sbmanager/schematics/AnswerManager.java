package net.seliba.sbmanager.schematics;

/*
SchematicBrushManager created by Seliba
*/

import java.util.HashMap;
import org.bukkit.entity.Player;

public class AnswerManager {

  public enum AnswerType {
    CREATION_WEB_WEBSITE, CREATION_WEB_NAME, CREATION_CUSTOM_NAME;
  }

  private static HashMap<Player, AnswerType> requestedAnswers = new HashMap<>();

  public static void addRequest(Player player, AnswerType expectedAnswer) {
    if (hasRequest(player)) {
      return;
    }
    requestedAnswers.put(player, expectedAnswer);
  }

  public static boolean hasRequest(Player player) {
    return requestedAnswers.containsKey(player);
  }

  public static AnswerType getRequestType(Player player) {
    return requestedAnswers.get(player);
  }

  public static void removeRequest(Player player) {
    requestedAnswers.remove(player);
  }

}

package net.seliba.sbmanager.listener;

/*
SchematicBrushManager created by Seliba
*/

import net.seliba.sbmanager.nbt.NBTItem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerCommandPreProcessListener implements Listener {

    @EventHandler
    public void onCommandSend(PlayerCommandPreprocessEvent event) {
        ItemStack itemInHand = event.getPlayer().getInventory().getItemInMainHand();
        if(event.getMessage().startsWith("/br") || event.getMessage().startsWith("/brush")) {
            if(itemInHand != null) {
                NBTItem nbt = new NBTItem(itemInHand);
                nbt.setString("command", event.getMessage());
                itemInHand = nbt.getItem();
            }
        }
    }

}

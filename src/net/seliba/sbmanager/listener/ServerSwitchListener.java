package net.seliba.sbmanager.listener;

/*
SchematicBrushManager created by Seliba
*/

import net.seliba.sbmanager.files.FileLoader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.IOException;

public class ServerSwitchListener implements Listener {

    private FileLoader fileLoader;

    public ServerSwitchListener(FileLoader fileLoader) {
        this.fileLoader = fileLoader;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) throws IOException {
        fileLoader.loadData(event.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) throws IOException {
        fileLoader.saveData(event.getPlayer());
    }

}

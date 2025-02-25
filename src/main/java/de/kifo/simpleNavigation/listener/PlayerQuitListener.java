package de.kifo.simpleNavigation.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import static de.kifo.simpleNavigation.Main.playerService;
import static org.bukkit.event.EventPriority.HIGHEST;

public class PlayerQuitListener implements Listener {

    @EventHandler(priority = HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        playerService.savePlayer(event.getPlayer());
    }
}

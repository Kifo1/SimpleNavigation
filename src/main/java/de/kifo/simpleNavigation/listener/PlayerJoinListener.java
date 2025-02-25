package de.kifo.simpleNavigation.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static de.kifo.simpleNavigation.Main.playerService;
import static org.bukkit.event.EventPriority.HIGHEST;

public class PlayerJoinListener implements Listener {

    @EventHandler (priority = HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        playerService.loadPlayer(event.getPlayer());
    }
}

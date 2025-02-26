package de.kifo.simpleNavigation.common.service;

import de.kifo.simpleNavigation.Main;
import de.kifo.simpleNavigation.common.files.database.entities.NaviPlayer;
import lombok.Data;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

import static de.kifo.simpleNavigation.Main.database;

@Data
public class PlayerService {

    private final Main main;

    private static Set<NaviPlayer> naviPlayers = new HashSet();

    public void loadPlayer(Player player) {
        NaviPlayer naviPlayer = database.loadNaviPlayer(player.getUniqueId());
        naviPlayers.add(naviPlayer);
    }

    public void savePlayer(Player player) {
        NaviPlayer naviPlayer = getNaviPlayer(player);
        database.saveNaviPlayer(naviPlayer);
        naviPlayers.remove(naviPlayer);
    }

    public void handleShutdown() {
        naviPlayers.forEach(naviPlayer -> database.saveNaviPlayer(naviPlayer));
    }

    public static NaviPlayer getNaviPlayer(Player player) {
        return naviPlayers.stream()
                .filter(naviPlayer -> naviPlayer.getUuid().equals(player.getUniqueId()))
                .findFirst()
                .orElse(null);
    }
}

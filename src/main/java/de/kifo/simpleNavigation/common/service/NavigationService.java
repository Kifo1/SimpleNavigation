package de.kifo.simpleNavigation.common.service;

import de.kifo.simpleNavigation.common.enums.NavigationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class NavigationService {

    private Set<Navigation> navigations = new HashSet<>();

    public void startPlayerNavigation(Player player, Location location, NavigationType type) {
        switch (type) {
            case COMPASS -> startCompassNavigation(player, location);
            case BOSSBAR -> startBossbarNavigation(player, location);
            case PARTICLES -> startParticleNavigation(player, location);
        }
    }

    private void startCompassNavigation(Player player, Location location) {
        //TODO Add compass to player inventory and navigate player
    }

    private void startBossbarNavigation(Player player, Location location) {
        //TODO Start task to update bossbar navigation
    }

    private void startParticleNavigation(Player player, Location location) {
        //TODO Start task to update particle line
    }

    @Data
    @AllArgsConstructor
    public static class Navigation {

        private final Player player;
        private final NavigationType type;

    }
}

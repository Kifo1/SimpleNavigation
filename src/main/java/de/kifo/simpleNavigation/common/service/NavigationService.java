package de.kifo.simpleNavigation.common.service;

import de.kifo.simpleNavigation.Main;
import de.kifo.simpleNavigation.common.enums.NavigationType;
import de.kifo.simpleNavigation.common.navigation.BossbarNavigation;
import de.kifo.simpleNavigation.common.navigation.CompassNavigation;
import de.kifo.simpleNavigation.common.navigation.ParticleNavigation;
import de.kifo.simpleNavigation.common.navigation.handle.Navigation;
import lombok.Data;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Data
public class NavigationService {

    private final Main main;

    private Set<Navigation> navigations = new HashSet<>();

    public void startPlayerNavigation(Player player, Location location, NavigationType type) {
        Navigation navigation = null;
        location = location.toCenterLocation().add(0, 0.5D, 0);

        switch (type) {
            case COMPASS -> navigation = new CompassNavigation(main, player, location, type);
            case BOSSBAR -> navigation = new BossbarNavigation(main, player, location, type);
            case PARTICLES -> navigation = new ParticleNavigation(main, player, location, type);
        }

        navigation.start();
        navigations.add(navigation);
    }

    public boolean stopNavigation(Player player) {
        Optional<Navigation> navigationOptional = navigations.stream()
                .filter(navigation -> navigation.getPlayer().equals(player))
                .findFirst();

        navigationOptional.ifPresent(navigation -> {
            navigations.remove(navigation);
            navigation.stop();
        });

        return navigationOptional.isPresent();
    }

    public boolean isNavigationRunning(Player player) {
        return navigations.stream().anyMatch(navigation -> navigation.getPlayer().equals(player));
    }
}

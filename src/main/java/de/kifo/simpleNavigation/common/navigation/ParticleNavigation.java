package de.kifo.simpleNavigation.common.navigation;

import de.kifo.simpleNavigation.Main;
import de.kifo.simpleNavigation.common.enums.NavigationType;
import de.kifo.simpleNavigation.common.navigation.handle.Navigation;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ParticleNavigation extends Navigation {

    public ParticleNavigation(Main main, Player player, Location location, NavigationType type) {
        super(main, player, location, type);
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}

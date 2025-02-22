package de.kifo.simpleNavigation.common.navigation.handle;

import de.kifo.simpleNavigation.Main;
import de.kifo.simpleNavigation.common.enums.NavigationType;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import static de.kifo.simpleNavigation.Main.configuration;
import static java.util.Objects.nonNull;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;

@Data
@RequiredArgsConstructor
public abstract class Navigation {

    private final Main main;
    private final Player player;
    private final NavigationTarget target;
    private final NavigationType type;
    private int taskId;

    public abstract void start();

    public abstract void stop();

    public boolean isTargetReached() {
        double distance = player.getLocation().distance(target.getTargetLocation());

        if (configuration.getBoolean("settings.message.distanceleft")) {
            player.sendActionBar(text((int) distance + " blocks away.", GOLD));
        }

        return distance < 2.0D;
    }

    public static class NavigationTarget {

        private Location location;
        private Player targetPlayer;

        public NavigationTarget(Location location) {
            this.location = location;
        }

        public NavigationTarget(Player player) {
            this.targetPlayer = player;
        }

        public Location getTargetLocation() {
            return nonNull(location) ? location : targetPlayer.getLocation();
        }
    }
}

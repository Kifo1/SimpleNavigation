package de.kifo.simpleNavigation.common.navigation.handle;

import de.kifo.simpleNavigation.Main;
import de.kifo.simpleNavigation.common.enums.NavigationType;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import static de.kifo.simpleNavigation.Main.configuration;
import static de.kifo.simpleNavigation.Main.navigationService;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.util.Objects.nonNull;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static org.bukkit.Particle.WAX_OFF;

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

    public void runNavigationChecks() {
        Location targetLocation = target.getTargetLocation();
        double distance = player.getLocation().distance(targetLocation);

        if ((nonNull(target.getTargetPlayer()) && !target.getTargetPlayer().isOnline()) || distance < 2.0D) {
            navigationService.stopNavigation(player);
            return;
        }

        if (configuration.getBoolean("settings.message.distanceleft")) {
            player.sendActionBar(text((int) distance + " blocks away.", GOLD));
        }

        for (int d = 0; d <= 90; d++) {
            Location particleLocation = targetLocation.clone();
            particleLocation.setX(targetLocation.getX() + cos(d));
            particleLocation.setZ(targetLocation.getZ() + sin(d));
            player.spawnParticle(WAX_OFF, particleLocation, 0, 0, 0, 0);
        }
    }

    public static class NavigationTarget {

        private Location location;
        @Getter
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

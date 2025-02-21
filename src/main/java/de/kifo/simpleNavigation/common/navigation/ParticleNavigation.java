package de.kifo.simpleNavigation.common.navigation;

import de.kifo.simpleNavigation.Main;
import de.kifo.simpleNavigation.common.enums.NavigationType;
import de.kifo.simpleNavigation.common.navigation.handle.Navigation;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import static de.kifo.simpleNavigation.Main.navigationService;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static org.bukkit.Bukkit.getScheduler;
import static org.bukkit.Particle.FLAME;

public class ParticleNavigation extends Navigation {

    public ParticleNavigation(Main main, Player player, Location location, NavigationType type) {
        super(main, player, location, type);
    }

    @Override
    public void start() {
        int taskId = getScheduler().runTaskTimerAsynchronously(getMain(), () -> {
            Player player = getPlayer();
            Location startLocation = player.getLocation().clone().add(0.0D, 1.0D, 0.0D);
            Location targetLocation = getLocation();

            if (isTargetReached()) {
                navigationService.stopNavigation(player);
                return;
            }

            Vector directionVector = targetLocation.toVector().subtract(startLocation.toVector()).normalize();
            for (int i = 0; i <= 10; i++) {
                Location particleLocation = startLocation.clone().add(directionVector.clone().multiply(i * 0.5D));

                double distance = targetLocation.distance(startLocation);
                if (particleLocation.distance(startLocation) <= distance) {
                    player.spawnParticle(FLAME, particleLocation, 0, 0, 0, 0);
                }

                for (int d = 0; d <= 90; d++) {
                    particleLocation = targetLocation.clone();
                    particleLocation.setX(targetLocation.getX() + cos(d));
                    particleLocation.setZ(targetLocation.getZ() + sin(d));
                    player.spawnParticle(FLAME, particleLocation, 0, 0, 0, 0);
                }
            }
        }, 10L, 10L).getTaskId();

        setTaskId(taskId);
    }

    @Override
    public void stop() {
        getScheduler().cancelTask(getTaskId());
    }
}

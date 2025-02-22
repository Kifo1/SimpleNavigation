package de.kifo.simpleNavigation.common.navigation;

import de.kifo.simpleNavigation.Main;
import de.kifo.simpleNavigation.common.enums.NavigationType;
import de.kifo.simpleNavigation.common.navigation.handle.Navigation;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.concurrent.atomic.AtomicInteger;

import static de.kifo.simpleNavigation.Main.navigationService;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static org.bukkit.Bukkit.getScheduler;
import static org.bukkit.Particle.WAX_OFF;

public class ParticleNavigation extends Navigation {

    public ParticleNavigation(Main main, Player player, NavigationTarget target, NavigationType type) {
        super(main, player, target, type);
    }

    @Override
    public void start() {
        AtomicInteger scheduler = new AtomicInteger();

        int taskId = getScheduler().runTaskTimerAsynchronously(getMain(), () -> {
            runNavigationChecks();

            Player player = getPlayer();
            Location startLocation = player.getLocation().clone().add(0.0D, 1.0D, 0.0D);
            Location targetLocation = getTarget().getTargetLocation();

            scheduler.getAndIncrement();
            if (scheduler.get() % 10 != 0) { //Send particles only 2 times per second
                return;
            }

            Vector directionVector = targetLocation.toVector().subtract(startLocation.toVector()).normalize();
            for (int i = 0; i <= 10; i++) {
                Location particleLocation = startLocation.clone().add(directionVector.clone().multiply(i * 0.5D));

                double distance = targetLocation.distance(startLocation);
                if (particleLocation.distance(startLocation) <= distance) {
                    player.spawnParticle(WAX_OFF, particleLocation, 0, 0, 0, 0);
                }

                for (int d = 0; d <= 90; d++) {
                    particleLocation = targetLocation.clone();
                    particleLocation.setX(targetLocation.getX() + cos(d));
                    particleLocation.setZ(targetLocation.getZ() + sin(d));
                    player.spawnParticle(WAX_OFF, particleLocation, 0, 0, 0, 0);
                }
            }
        }, 1L, 1L).getTaskId();

        setTaskId(taskId);
    }

    @Override
    public void stop() {
        getScheduler().cancelTask(getTaskId());
    }
}

package de.kifo.simpleNavigation.common.navigation;

import de.kifo.simpleNavigation.Main;
import de.kifo.simpleNavigation.common.enums.NavigationType;
import de.kifo.simpleNavigation.common.navigation.handle.Navigation;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import static de.kifo.simpleNavigation.Main.navigationService;
import static org.bukkit.Bukkit.getScheduler;

public class BossbarNavigation extends Navigation {

    public BossbarNavigation(Main main, Player player, Location location, NavigationType type) {
        super(main, player, location, type);
    }

    @Override
    public void start() {
        int taskId = getScheduler().runTaskTimer(getMain(), () -> {
            Player player = getPlayer();
            Location targetLocation = getLocation();

            if (isTargetReached()) {
                navigationService.stopNavigation(player);
            }

            Vector targetDirection = targetLocation.toVector().setY(0).subtract(player.getLocation().toVector().setY(0));
            float playerYaw = player.getLocation().getYaw();
            float targetDirectionYaw = player.getLocation().clone().setDirection(targetDirection).getYaw();
            targetDirectionYaw = targetDirectionYaw > 180 ? -360 + targetDirectionYaw : targetDirectionYaw; //Change target direction yaw range from 0-360 to -180-180 (similar to player yaw range)
            playerYaw -= targetDirectionYaw; // Subtract target direction yaw from player yaw to create a yaw system with target direction at 0
            float deltaYaw = playerYaw > 180 ? 360 - playerYaw : playerYaw > 0 ? playerYaw : playerYaw < -180 ? -360 - playerYaw : playerYaw;
            System.out.println(deltaYaw);

            boolean isTargetLeftForPlayer = (playerYaw > 0 && playerYaw < 180) || playerYaw < -180;  //Check if player yaw is to the left or right side of 0 with -180/180 on the opposite site of 0
            if (isTargetLeftForPlayer) {
                System.out.println("Target is to your left.");
            } else {
                System.out.println("Target is to your right.");
            }
        }, 10L, 10L).getTaskId();

        setTaskId(taskId);
    }

    @Override
    public void stop() {
        getScheduler().cancelTask(getTaskId());
    }
}

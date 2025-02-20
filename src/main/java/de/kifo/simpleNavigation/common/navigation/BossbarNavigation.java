package de.kifo.simpleNavigation.common.navigation;

import de.kifo.simpleNavigation.Main;
import de.kifo.simpleNavigation.common.enums.NavigationType;
import de.kifo.simpleNavigation.common.navigation.handle.Navigation;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import static de.kifo.simpleNavigation.Main.navigationService;
import static java.lang.Math.abs;
import static net.kyori.adventure.text.Component.text;
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
            float deltaYaw = abs(playerYaw > 180 ? 360 - playerYaw : playerYaw > 0 ? playerYaw : playerYaw < -180 ? -360 - playerYaw : playerYaw);

            boolean isTargetLeftForPlayer = (playerYaw > 0 && playerYaw < 180) || playerYaw < -180;  //Check if player yaw is to the left or right side of 0 with -180/180 on the opposite site of 0
            player.sendMessage(getBossBarComponent((int) deltaYaw, isTargetLeftForPlayer));
        }, 10L, 10L).getTaskId();

        setTaskId(taskId);
    }

    private Component getBossBarComponent(int deltaYaw, boolean isTargetLeft) {
        if (deltaYaw <= 10) {
            return text("---------> <---------");
        }

        if (deltaYaw >= 100) {
            return isTargetLeft ? text("---------- ---------<") : text(">--------- ----------");
        }

        String componentText = "";
        if (isTargetLeft) {
            componentText += "---------- ";
            for (int i = 0; i < 10; i++) {
                int stageYaw = (deltaYaw - 10) - i * 10;
                boolean isArrow = stageYaw < 0 && stageYaw >= -10;
                componentText += isArrow ? "<" : "-";
            }
        } else {
            for (int i = 0; i < 10; i++) {
                int stageYaw = (deltaYaw - 10) - i * 10;
                boolean isArrow = stageYaw < 0 && stageYaw >= -10;
                componentText = (isArrow ? ">" : "-") + componentText;
            }
            componentText += " ----------";
        }

        return text(componentText);
    }

    @Override
    public void stop() {
        getScheduler().cancelTask(getTaskId());
    }
}

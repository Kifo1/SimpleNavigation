package de.kifo.simpleNavigation.common.navigation;

import de.kifo.simpleNavigation.Main;
import de.kifo.simpleNavigation.common.enums.NavigationType;
import de.kifo.simpleNavigation.common.navigation.handle.Navigation;
import org.bukkit.Location;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import static de.kifo.simpleNavigation.Main.navigationService;
import static java.lang.Math.abs;
import static java.lang.Math.min;
import static org.bukkit.Bukkit.createBossBar;
import static org.bukkit.Bukkit.getScheduler;
import static org.bukkit.boss.BarColor.PURPLE;
import static org.bukkit.boss.BarStyle.SOLID;

public class BossbarNavigation extends Navigation {

    private BossBar bossBar;

    public BossbarNavigation(Main main, Player player, NavigationTarget target, NavigationType type) {
        super(main, player, target, type);
    }

    @Override
    public void start() {
        Player player = getPlayer();

        double startDistance = player.getLocation().distance(getTarget().getTargetLocation());

        this.bossBar = createBossBar("", PURPLE, SOLID);
        this.bossBar.addPlayer(player);
        this.bossBar.setProgress(1.0);

        int taskId = getScheduler().runTaskTimerAsynchronously(getMain(), () -> {
            Location targetLocation = getTarget().getTargetLocation();

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

            this.bossBar.setTitle(getBossBarComponent((int) deltaYaw, isTargetLeftForPlayer));

            double distance = player.getLocation().distance(targetLocation);
            float progress = (float) (distance / startDistance);
            this.bossBar.setProgress(min(progress, 1.0F));
        }, 1L, 1L).getTaskId();

        setTaskId(taskId);
    }

    private String getBossBarComponent(int deltaYaw, boolean isTargetLeft) {
        if (deltaYaw <= 10) {
            return "---------> <---------";
        }

        if (deltaYaw >= 100) {
            return isTargetLeft ? "---------- ---------<" : ">--------- ----------";
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

        return componentText;
    }

    @Override
    public void stop() {
        this.bossBar.removeAll();
        getScheduler().cancelTask(getTaskId());
    }
}

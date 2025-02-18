package de.kifo.simpleNavigation.common.navigation.handle;

import de.kifo.simpleNavigation.Main;
import de.kifo.simpleNavigation.common.enums.NavigationType;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@Data
@RequiredArgsConstructor
public abstract class Navigation {

    private final Main main;
    private final Player player;
    private final Location location;
    private final NavigationType type;
    private int taskId;

    public abstract void start();

    public abstract void stop();

    public boolean isTargetReached() {
        return player.getLocation().distance(location) < 2.0D;
    }
}

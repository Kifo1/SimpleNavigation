package de.kifo.simpleNavigation.common.files.database.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Location;

import java.util.UUID;

import static org.bukkit.Bukkit.getWorld;

@Data
@AllArgsConstructor
public class NaviPoint {

    private final String NaviPointName;
    private final String worldName;
    private final int x;
    private final int y;
    private final int z;
    private final UUID player;

    public Location getLocation() {
        return new Location(getWorld(worldName), x, y, z);
    }
}

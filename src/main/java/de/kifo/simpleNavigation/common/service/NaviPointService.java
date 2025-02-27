package de.kifo.simpleNavigation.common.service;

import de.kifo.simpleNavigation.Main;
import de.kifo.simpleNavigation.common.files.database.entities.NaviPoint;
import lombok.Data;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;

import static de.kifo.simpleNavigation.Main.database;

@Data
public class NaviPointService {

    private final Main main;

    public Set<NaviPoint> getAllNaviPointsByPlayer(UUID uuid) {
        return database.getNaviPointsByPlayer(uuid);
    }

    public void addNaviPoint(NaviPoint naviPoint) {
        database.addNaviPoint(naviPoint);
    }

    public void addNaviPoint(String name, Location location, Player player) {
        database.addNaviPoint(new NaviPoint(name, location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), player.getUniqueId()));
    }

    public NaviPoint getNaviPoint(String name, UUID uuid) {
        return database.getNaviPoint(name, uuid);
    }

    public void deleteNaviPoint(NaviPoint naviPoint) {
        database.deleteNaviPoint(naviPoint.getNaviPointName(), naviPoint.getPlayer());
    }

    public void deleteNaviPoint(String name, UUID uuid) {
        database.deleteNaviPoint(name, uuid);
    }
}

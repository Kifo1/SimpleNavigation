package de.kifo.simpleNavigation.common.service;

import de.kifo.simpleNavigation.Main;
import de.kifo.simpleNavigation.common.files.database.entities.NaviPoint;
import lombok.Data;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;

import static de.kifo.simpleNavigation.Main.database;
import static java.util.Objects.nonNull;

@Data
public class NaviPointService {

    private final Main main;

    public Set<NaviPoint> getAllNaviPointsByPlayer(UUID uuid) {
        return database.getNaviPointsByPlayer(uuid);
    }

    public void addNaviPoint(NaviPoint naviPoint) {
        database.addNaviPoint(naviPoint);
    }

    public void addNaviPoint(String name, Location location, UUID uuid) {
        database.addNaviPoint(new NaviPoint(name, location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), uuid));
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

    public boolean naviPointExists(String name, UUID uuid) {
        return nonNull(database.getNaviPoint(name, uuid));
    }

    // See all navi points
    // <add> Add a new navi point
    // <remove> delete a navi point
}

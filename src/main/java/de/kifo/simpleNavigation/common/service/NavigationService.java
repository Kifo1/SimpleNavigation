package de.kifo.simpleNavigation.common.service;

import de.kifo.simpleNavigation.Main;
import de.kifo.simpleNavigation.common.enums.NavigationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

import static de.kifo.simpleNavigation.Main.itemService;
import static org.bukkit.Material.COMPASS;
import static org.bukkit.persistence.PersistentDataType.BOOLEAN;

@Data
public class NavigationService {

    private final Main main;

    private Set<Navigation> navigations = new HashSet<>();

    public void startPlayerNavigation(Player player, Location location, NavigationType type) {
        switch (type) {
            case COMPASS -> startCompassNavigation(player, location);
            case BOSSBAR -> startBossbarNavigation(player, location);
            case PARTICLES -> startParticleNavigation(player, location);
        }
    }

    private void startCompassNavigation(Player player, Location location) {
        ItemStack compass = itemService.getBuilder()
                .material(COMPASS)
                .displayName("Navi")
                .itemData(new NamespacedKey(main, "navigationItem"), BOOLEAN, true)
                .naviLocation(location)
                .build();

        player.getInventory().addItem(compass);
    }

    private void startBossbarNavigation(Player player, Location location) {
        //TODO Start task to update bossbar navigation
    }

    private void startParticleNavigation(Player player, Location location) {
        //TODO Start task to update particle line
    }

    @Data
    @AllArgsConstructor
    public static class Navigation {

        private final Player player;
        private final NavigationType type;

    }
}

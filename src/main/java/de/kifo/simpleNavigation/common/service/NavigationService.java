package de.kifo.simpleNavigation.common.service;

import de.kifo.simpleNavigation.Main;
import de.kifo.simpleNavigation.common.enums.NavigationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static de.kifo.simpleNavigation.Main.itemService;
import static de.kifo.simpleNavigation.common.service.ItemService.NAVI_ITEM_KEY;
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

        navigations.add(new Navigation(player, type));
    }

    private void startCompassNavigation(Player player, Location location) {
        ItemStack compass = itemService.getBuilder()
                .material(COMPASS)
                .displayName("Navi")
                .itemData(NAVI_ITEM_KEY, BOOLEAN, true)
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

    public boolean stopNavigation(Player player) {
        Optional<Navigation> navigationOptional = navigations.stream()
                .filter(navigation -> navigation.getPlayer().equals(player))
                .findFirst();
        navigationOptional.ifPresent(navigation -> navigations.remove(navigation));

        return navigationOptional.isPresent();
    }

    @Data
    @AllArgsConstructor
    public static class Navigation {

        private final Player player;
        private final NavigationType type;

    }
}

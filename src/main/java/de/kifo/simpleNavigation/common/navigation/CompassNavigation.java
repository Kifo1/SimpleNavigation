package de.kifo.simpleNavigation.common.navigation;

import de.kifo.simpleNavigation.common.enums.NavigationType;
import de.kifo.simpleNavigation.common.navigation.handle.Navigation;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static de.kifo.simpleNavigation.Main.itemService;
import static de.kifo.simpleNavigation.common.service.ItemService.NAVI_ITEM_KEY;
import static org.bukkit.Material.COMPASS;
import static org.bukkit.persistence.PersistentDataType.BOOLEAN;

public class CompassNavigation extends Navigation {

    public CompassNavigation(Player player, Location location, NavigationType type) {
        super(player, location, type);
    }

    @Override
    public void start() {
        ItemStack compass = itemService.getBuilder()
                .material(COMPASS)
                .displayName("Navi")
                .itemData(NAVI_ITEM_KEY, BOOLEAN, true)
                .naviLocation(getLocation())
                .build();

        getPlayer().getInventory().addItem(compass);
    }

    @Override
    public void stop() {

    }
}

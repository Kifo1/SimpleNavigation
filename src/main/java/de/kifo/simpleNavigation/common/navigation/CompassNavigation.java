package de.kifo.simpleNavigation.common.navigation;

import de.kifo.simpleNavigation.Main;
import de.kifo.simpleNavigation.common.enums.NavigationType;
import de.kifo.simpleNavigation.common.navigation.handle.Navigation;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static de.kifo.simpleNavigation.Main.itemService;
import static de.kifo.simpleNavigation.Main.navigationService;
import static de.kifo.simpleNavigation.common.service.ItemService.NAVI_ITEM_KEY;
import static org.bukkit.Bukkit.getScheduler;
import static org.bukkit.Material.COMPASS;
import static org.bukkit.persistence.PersistentDataType.BOOLEAN;

public class CompassNavigation extends Navigation {

    public CompassNavigation(Main main, Player player, Location location, NavigationType type) {
        super(main, player, location, type);
    }

    @Override
    public void start() {
        ItemStack compass = itemService.getBuilder()
                .material(COMPASS)
                .displayName("Navi")
                .itemData(NAVI_ITEM_KEY, BOOLEAN, true)
                .naviLocation(getLocation())
                .build();
        getPlayer().getInventory().addItem(compass); //TODO Check if inventory is full => Use specific slot for compass (e.g. off hand)

        int taskId = getScheduler().runTaskTimer(getMain(), () -> {
            if (isTargetReached()) {
                navigationService.stopNavigation(getPlayer());
            }
        }, 20L, 20L).getTaskId();

        this.setTaskId(taskId);
    }

    @Override
    public void stop() {
        itemService.removeAllNaviItems(getPlayer());
        getScheduler().cancelTask(getTaskId());
    }
}

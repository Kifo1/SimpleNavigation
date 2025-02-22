package de.kifo.simpleNavigation.common.navigation;

import de.kifo.simpleNavigation.Main;
import de.kifo.simpleNavigation.common.enums.NavigationType;
import de.kifo.simpleNavigation.common.navigation.handle.Navigation;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import static de.kifo.simpleNavigation.Main.itemService;
import static de.kifo.simpleNavigation.Main.navigationService;
import static de.kifo.simpleNavigation.common.service.ItemService.NAVI_ITEM_KEY;
import static org.bukkit.Bukkit.getScheduler;
import static org.bukkit.Material.COMPASS;
import static org.bukkit.inventory.EquipmentSlot.OFF_HAND;
import static org.bukkit.persistence.PersistentDataType.BOOLEAN;

public class CompassNavigation extends Navigation {

    private ItemStack offHandItem;

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

        PlayerInventory playerInventory = getPlayer().getInventory();
        this.offHandItem = playerInventory.getItem(OFF_HAND);
        playerInventory.setItem(OFF_HAND, compass);

        int taskId = getScheduler().runTaskTimerAsynchronously(getMain(), () -> {
            if (isTargetReached()) {
                navigationService.stopNavigation(getPlayer());
            }
        }, 1L, 1L).getTaskId();

        setTaskId(taskId);
    }

    @Override
    public void stop() {
        itemService.removeAllNaviItems(getPlayer());
        getPlayer().getInventory().setItem(OFF_HAND, this.offHandItem);
        getScheduler().cancelTask(getTaskId());
    }
}

package de.kifo.simpleNavigation.common.navigation;

import de.kifo.simpleNavigation.Main;
import de.kifo.simpleNavigation.common.enums.NavigationType;
import de.kifo.simpleNavigation.common.navigation.handle.Navigation;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.CompassMeta;

import static de.kifo.simpleNavigation.Main.itemService;
import static de.kifo.simpleNavigation.common.service.ItemService.NAVI_ITEM_KEY;
import static org.bukkit.Bukkit.getScheduler;
import static org.bukkit.Material.COMPASS;
import static org.bukkit.inventory.EquipmentSlot.OFF_HAND;
import static org.bukkit.persistence.PersistentDataType.BOOLEAN;

public class CompassNavigation extends Navigation {

    private ItemStack offHandItem;
    private ItemStack compassItem;

    public CompassNavigation(Main main, Player player, NavigationTarget target, NavigationType type) {
        super(main, player, target, type);
    }

    @Override
    public void start() {
        compassItem = itemService.getBuilder()
                .material(COMPASS)
                .displayName("Navi")
                .itemData(NAVI_ITEM_KEY, BOOLEAN, true)
                .naviLocation(getTarget().getTargetLocation())
                .build();

        PlayerInventory playerInventory = getPlayer().getInventory();
        this.offHandItem = playerInventory.getItem(OFF_HAND);
        playerInventory.setItem(OFF_HAND, compassItem);

        int taskId = getScheduler().runTaskTimer(getMain(), () -> {
            runNavigationChecks();

            CompassMeta compassMeta = (CompassMeta) compassItem.getItemMeta();
            compassMeta.setLodestone(getTarget().getTargetLocation());
            compassMeta.setLodestoneTracked(false);
            playerInventory.getItemInOffHand().setItemMeta(compassMeta);
        }, 1L, 1L).getTaskId();

        setTaskId(taskId);
    }

    @Override
    public void stop() {
        getScheduler().cancelTask(getTaskId());
        getPlayer().getInventory().setItem(OFF_HAND, this.offHandItem);
        itemService.removeAllNaviItems(getPlayer());
    }
}

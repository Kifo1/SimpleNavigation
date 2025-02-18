package de.kifo.simpleNavigation.listener;

import io.papermc.paper.event.player.PlayerItemFrameChangeEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import static de.kifo.simpleNavigation.Main.itemService;
import static de.kifo.simpleNavigation.common.service.ItemService.NAVI_ITEM_KEY;
import static java.util.Arrays.stream;
import static java.util.Objects.nonNull;

public class NaviItemProtectListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        stream(player.getInventory().getContents())
                .filter(itemStack -> nonNull(itemStack) && nonNull(itemStack.getItemMeta()))
                .filter(itemStack -> itemStack.getItemMeta().getPersistentDataContainer().has(NAVI_ITEM_KEY))
                .forEach(itemStack -> {
                    player.getInventory().remove(itemStack);
                });
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        ItemStack itemStack = event.getItemDrop().getItemStack();

        if (itemService.isNaviItem(itemStack)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.getDrops().removeIf(itemStack -> itemService.isNaviItem(itemStack));
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (itemService.isNaviItem(event.getCurrentItem())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerItemFrameChangeEvent event) {
        if (itemService.isNaviItem(event.getItemStack())) {
            event.setCancelled(true);
        }
    }
}

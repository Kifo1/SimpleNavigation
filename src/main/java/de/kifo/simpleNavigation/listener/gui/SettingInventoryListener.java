package de.kifo.simpleNavigation.listener.gui;

import de.kifo.simpleNavigation.common.enums.NavigationType;
import de.kifo.simpleNavigation.common.files.database.entities.NaviPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import static de.kifo.simpleNavigation.common.enums.NavigationType.BOSSBAR;
import static de.kifo.simpleNavigation.common.enums.NavigationType.PARTICLES;
import static de.kifo.simpleNavigation.common.inventories.SettingsInventory.settingInventories;
import static de.kifo.simpleNavigation.common.inventories.SettingsInventory.update;
import static de.kifo.simpleNavigation.common.service.PlayerService.getNaviPlayer;

public class SettingInventoryListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();

        if (!settingInventories.contains(inventory)) {
            return;
        }
        event.setCancelled(true);

        NaviPlayer naviPlayer = getNaviPlayer((Player) event.getWhoClicked());
        Material itemMaterial = event.getCurrentItem().getType();
        switch (itemMaterial) {
            case COMPASS -> naviPlayer.setPreferredNavigationType(NavigationType.COMPASS);
            case WITHER_SKELETON_SKULL -> naviPlayer.setPreferredNavigationType(BOSSBAR);
            case GUNPOWDER -> naviPlayer.setPreferredNavigationType(PARTICLES);
        }

        update(naviPlayer, inventory);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        settingInventories.removeIf(settingInventory -> settingInventory.equals(inventory));
    }
}

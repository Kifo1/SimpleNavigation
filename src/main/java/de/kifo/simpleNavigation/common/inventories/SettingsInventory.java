package de.kifo.simpleNavigation.common.inventories;

import de.kifo.simpleNavigation.common.enums.NavigationType;
import de.kifo.simpleNavigation.common.files.database.entities.NaviPlayer;
import org.bukkit.inventory.Inventory;

import java.util.HashSet;
import java.util.Set;

import static de.kifo.simpleNavigation.Main.itemService;
import static de.kifo.simpleNavigation.common.enums.NavigationType.BOSSBAR;
import static de.kifo.simpleNavigation.common.enums.NavigationType.PARTICLES;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.NamedTextColor.WHITE;
import static org.bukkit.Bukkit.createInventory;
import static org.bukkit.Material.COMPASS;
import static org.bukkit.Material.GRAY_STAINED_GLASS_PANE;
import static org.bukkit.Material.GUNPOWDER;
import static org.bukkit.Material.WITHER_SKELETON_SKULL;

public class SettingsInventory {

    public static Set<Inventory> settingInventories = new HashSet();

    public static Inventory create(NaviPlayer naviPlayer) {
        Inventory inventory = createInventory(null, 27, "Settings");
        settingInventories.add(inventory);
        update(naviPlayer, inventory);
        return inventory;
    }

    public static void update(NaviPlayer naviPlayer, Inventory inventory) {
        for (int i = 0; i < 27; i++) {
            inventory.setItem(i, itemService.getBuilder()
                    .material(GRAY_STAINED_GLASS_PANE)
                    .displayName(text(" "))
                    .lore(text(" "))
                    .build());
        }

        NavigationType preferredNavigationType = naviPlayer.getPreferredNavigationType();

        inventory.setItem(11, itemService.getBuilder()
                .material(COMPASS)
                .displayName("Compass", WHITE)
                .lore(preferredNavigationType.equals(NavigationType.COMPASS) ? text("Currently active", GREEN) : text("Click to choose", RED))
                .build());

        inventory.setItem(13, itemService.getBuilder()
                .material(WITHER_SKELETON_SKULL)
                .displayName("Bossbar", WHITE)
                .lore(preferredNavigationType.equals(BOSSBAR) ? text("Currently active", GREEN) : text("Click to choose", RED))
                .build());

        inventory.setItem(15, itemService.getBuilder()
                .material(GUNPOWDER)
                .displayName("Particles", WHITE)
                .lore(preferredNavigationType.equals(PARTICLES) ? text("Currently active", GREEN) : text("Click to choose", RED))
                .build());
    }
}

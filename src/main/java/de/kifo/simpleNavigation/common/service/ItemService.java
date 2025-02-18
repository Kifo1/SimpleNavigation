package de.kifo.simpleNavigation.common.service;

import de.kifo.simpleNavigation.Main;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

import static com.google.common.collect.ImmutableList.of;
import static java.util.Arrays.stream;
import static java.util.Objects.nonNull;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static org.bukkit.Material.AIR;

@Data
public class ItemService {

    private final Main main;

    public static final NamespacedKey NAVI_ITEM_KEY = new NamespacedKey("simple_navigation", "navigation_item");

    public Builder getBuilder() {
        return new Builder();
    }

    public void removeAllNaviItems(Player player) {
        stream(player.getInventory().getContents())
                .filter(itemStack -> isNaviItem(itemStack))
                .forEach(itemStack -> {
                    player.getInventory().remove(itemStack);
                });
    }

    public boolean isNaviItem(ItemStack itemStack) {
        return nonNull(itemStack) && nonNull(itemStack.getItemMeta())
                && itemStack.getItemMeta().getPersistentDataContainer().has(NAVI_ITEM_KEY);
    }

    @NoArgsConstructor
    public class Builder {

        private Material material = AIR;
        private int amount = 1;
        private Component displayName = text("");
        private List<Component> lore = of();
        private ItemData itemData = null;
        private Location naviLocation = null;

        public Builder material(Material material) {
            this.material = material;
            return this;
        }

        public Builder amount(int amount) {
            this.amount = amount;
            return this;
        }

        public Builder displayName(Component displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder displayName(String name) {
            this.displayName = text(name, GRAY);
            return this;
        }

        public Builder displayName(String name, NamedTextColor color) {
            this.displayName = text(name, color);
            return this;
        }

        public Builder lore(List<Component> lore) {
            this.lore = lore;
            return this;
        }

        public Builder lore(Component... lore) {
            this.lore = stream(lore).toList();
            return this;
        }

        public Builder itemData(ItemData itemData) {
            this.itemData = itemData;
            return this;
        }

        public Builder itemData(NamespacedKey key, PersistentDataType type, Object value) {
            this.itemData = new ItemData(key, type, value);
            return this;
        }

        public Builder naviLocation(Location naviLocation) {
            this.naviLocation = naviLocation;
            return this;
        }

        public ItemStack build() {
            ItemStack itemStack = new ItemStack(material, amount);
            ItemMeta itemMeta = itemStack.getItemMeta();

            itemMeta.displayName(this.displayName);
            itemMeta.lore(this.lore);

            if (nonNull(this.itemData)) {
                itemMeta.getPersistentDataContainer()
                        .set(this.itemData.namespacedKey, this.itemData.persistentDataType, this.itemData.value);
            }
            itemStack.setItemMeta(itemMeta);

            if (nonNull(this.naviLocation)) {
                CompassMeta compassMeta = (CompassMeta) itemStack.getItemMeta();
                compassMeta.setLodestone(this.naviLocation);
                compassMeta.setLodestoneTracked(false);
                itemStack.setItemMeta(compassMeta);
            }

            return itemStack;
        }
    }

    @Getter
    @AllArgsConstructor
    private class ItemData {

        private final NamespacedKey namespacedKey;
        private final PersistentDataType persistentDataType;
        private final Object value;

    }
}

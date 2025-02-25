package de.kifo.simpleNavigation.command;

import de.kifo.simpleNavigation.common.files.database.entities.NaviPlayer;
import de.kifo.simpleNavigation.common.inventories.SettingsInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static de.kifo.simpleNavigation.Main.configuration;
import static de.kifo.simpleNavigation.common.service.PlayerService.getNaviPlayer;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

public class SettingsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(text("This command can only be used by players", RED));
            return false;
        }

        if (!player.hasPermission(configuration.getString("permission.command.settings"))) {
            player.sendMessage(text("You don't have permission to use this command", RED));
            return false;
        }

        NaviPlayer naviPlayer = getNaviPlayer(player);
        player.openInventory(SettingsInventory.create(naviPlayer));

        return true;
    }
}

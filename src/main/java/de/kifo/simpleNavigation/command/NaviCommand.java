package de.kifo.simpleNavigation.command;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.google.common.collect.ImmutableList.of;
import static de.kifo.simpleNavigation.Main.configuration;
import static de.kifo.simpleNavigation.Main.navigationService;
import static de.kifo.simpleNavigation.common.service.PlayerService.getNaviPlayer;
import static java.lang.Integer.parseInt;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static org.bukkit.Bukkit.getOnlinePlayers;

public class NaviCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(text("This command can only be used by players.", RED));
            return false;
        }

        if (!player.hasPermission(configuration.getString("permission.command.navi"))) {
            player.sendMessage(text("You do not have permission to use this command.", RED));
            return false;
        }

        switch (strings.length) {
            case 0, 1 -> {
                if (strings.length == 1 && !strings[0].equalsIgnoreCase("stop")) {
                    sendUsage(player);
                }
                if (navigationService.stopNavigation(player)) {
                    player.sendMessage(text("You have stopped your navigation.", GREEN));
                } else {
                    player.sendMessage(text("Navigation is not running.", RED));
                }
            }
            case 3 -> {
                int x, y, z;
                try {
                    x = parseInt(strings[0]);
                    y = parseInt(strings[1]);
                    z = parseInt(strings[2]);
                } catch (Exception e) {
                    player.sendMessage(text("Coordinates have to be numbers.", RED));
                    return false;
                }

                navigationService.startPlayerNavigation(getNaviPlayer(player), new Location(player.getWorld(), x, y, z));
            }
            default -> sendUsage(player);
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> options;
        switch (strings.length) {
            case 1 -> options = of("<x>");
            case 2 -> options = of("<y>");
            case 3 -> options = of("<z>");
            default -> options = getOnlinePlayers().stream().map(p -> p.getName()).toList();
        }
        return options;
    }

    private void sendUsage(Player player) {
        player.sendMessage(text("Use: /navi <x> <y> <z>", GRAY));
    }
}

package de.kifo.simpleNavigation.command;

import de.kifo.simpleNavigation.common.enums.NavigationType;
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
import static de.kifo.simpleNavigation.Main.navigationService;
import static java.lang.Integer.parseInt;
import static java.util.Arrays.stream;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static org.bukkit.Bukkit.getOnlinePlayers;

public class NaviCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(text("This command can only be used by players.", RED));
            return false;
        }

        if (strings.length != 4) {
            sendUsage(commandSender);
            return false;
        }

        int x, y, z;
        try {
            x = parseInt(strings[1]);
            y = parseInt(strings[2]);
            z = parseInt(strings[3]);
        } catch (Exception e) {
            player.sendMessage(text("Coordinates have to be numbers.", RED));
            return false;
        }

        stream(NavigationType.values())
                .filter(type -> type.getDisplayName().equalsIgnoreCase(strings[0]))
                .findFirst()
                .ifPresentOrElse((type) -> {
                    navigationService.startPlayerNavigation(player, new Location(player.getWorld(), x, y, z), type);
                }, () -> {
                    player.sendMessage(text("Navigation type couldn't be found.", RED));
                });

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> options;
        switch (strings.length) {
            case 1 -> options = stream(NavigationType.values()).map(navigationType -> navigationType.getDisplayName()).toList();
            case 2 -> options = of("<x>");
            case 3 -> options = of("<y>");
            case 4 -> options = of("<z>");
            default -> options = getOnlinePlayers().stream().map(p -> p.getName()).toList();
        }
        return options;
    }

    private void sendUsage(CommandSender commandSender) {
        commandSender.sendMessage(text("Use: /navi <type> <x> <y> <z>", GRAY));
    }
}

package de.kifo.simpleNavigation.command;

import de.kifo.simpleNavigation.common.enums.NavigationType;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static de.kifo.simpleNavigation.Main.navigationService;
import static java.lang.Integer.parseInt;
import static java.util.Arrays.stream;
import static java.util.Objects.isNull;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static org.bukkit.Bukkit.getPlayer;

public class ConsoleNaviCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof ConsoleCommandSender)) {
            commandSender.sendMessage("§cThis command can only be used by the console.");
            return false;
        }

        if (strings.length != 5) {
            sendUsage(commandSender);
            return false;
        }

        int x, y, z;
        try {
            x = parseInt(strings[1]);
            y = parseInt(strings[2]);
            z = parseInt(strings[3]);
        } catch (Exception e) {
            commandSender.sendMessage("Coordinates have to be numbers.");
            return false;
        }

        Player target = getPlayer(strings[4]);
        if (isNull(target)) {
            commandSender.sendMessage("Player \"" + strings[4] + "\" couldn't be found.");
            return false;
        }

        stream(NavigationType.values())
                .filter(type -> type.getDisplayName().equalsIgnoreCase(strings[0]))
                .findFirst()
                .ifPresentOrElse((type) -> {
                    navigationService.startPlayerNavigation(target, new Location(target.getWorld(), x, y, z), type);
                }, () -> commandSender.sendMessage("Navigation type couldn't be found."));

        return true;
    }

    private void sendUsage(CommandSender commandSender) {
        commandSender.sendMessage(text("Use: /consolenavi <type> <x> <y> <z> <player>", GRAY));
    }
}

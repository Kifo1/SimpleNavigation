package de.kifo.simpleNavigation.command;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static de.kifo.simpleNavigation.Main.navigationService;
import static de.kifo.simpleNavigation.common.service.PlayerService.getNaviPlayer;
import static java.lang.Integer.parseInt;
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

        if (strings.length != 4) {
            sendUsage(commandSender);
            return false;
        }

        int x, y, z;
        try {
            x = parseInt(strings[0]);
            y = parseInt(strings[1]);
            z = parseInt(strings[2]);
        } catch (Exception e) {
            commandSender.sendMessage("Coordinates have to be numbers.");
            return false;
        }

        Player target = getPlayer(strings[3]);
        if (isNull(target)) {
            commandSender.sendMessage("Player \"" + strings[3] + "\" couldn't be found.");
            return false;
        }

        navigationService.startPlayerNavigation(getNaviPlayer(target), new Location(target.getWorld(), x, y, z));
        return true;
    }

    private void sendUsage(CommandSender commandSender) {
        commandSender.sendMessage(text("Use: /consolenavi <x> <y> <z> <player>", GRAY));
    }
}

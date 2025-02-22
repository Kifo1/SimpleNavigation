package de.kifo.simpleNavigation.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static de.kifo.simpleNavigation.Main.configuration;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

public class ReloadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!commandSender.hasPermission(configuration.getString("permission.command.reload"))) {
            commandSender.sendMessage(text("You don't have permission to use this command!", RED));
            return false;
        }

        configuration.reloadConfig();
        commandSender.sendMessage(text("Configuration reloaded!", GREEN));

        return true;
    }
}

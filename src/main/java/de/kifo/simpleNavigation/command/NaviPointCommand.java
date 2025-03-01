package de.kifo.simpleNavigation.command;

import de.kifo.simpleNavigation.common.files.database.entities.NaviPoint;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.google.common.collect.ImmutableList.of;
import static de.kifo.simpleNavigation.Main.configuration;
import static de.kifo.simpleNavigation.Main.naviPointService;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.event.ClickEvent.Action.RUN_COMMAND;
import static net.kyori.adventure.text.event.ClickEvent.clickEvent;
import static net.kyori.adventure.text.event.HoverEvent.Action.SHOW_TEXT;
import static net.kyori.adventure.text.event.HoverEvent.hoverEvent;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.NamedTextColor.WHITE;
import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;
import static org.bukkit.Bukkit.getOnlinePlayers;

public class NaviPointCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(text("This command can only be used by players.", RED));
            return false;
        }

        if (!player.hasPermission(configuration.getString("permission.command.navipoint"))) {
            player.sendMessage(text("You don't have permission to use this command.", RED));
            return false;
        }

        if (strings.length != 1 && strings.length != 2) {
            sendUsage(player);
            return false;
        }

        UUID uuid = player.getUniqueId();
        if (strings.length == 1) {
            if (!strings[0].equalsIgnoreCase("list")) {
                sendUsage(player);
                return false;
            }

            Set<NaviPoint> naviPoints = naviPointService.getAllNaviPointsByPlayer(uuid);
            if (naviPoints.size() == 0) {
                player.sendMessage(text("You don't have any navi points.", RED));
                return false;
            }

            player.sendMessage(text("Navi points:", GOLD));
            naviPointService.getAllNaviPointsByPlayer(uuid).forEach(naviPoint -> {
                Component component = text("»", GRAY)
                        .appendSpace()
                        .append(text(naviPoint.getNaviPointName(), YELLOW))
                        .hoverEvent(hoverEvent(SHOW_TEXT, text("Click to start navigation.", WHITE)))
                        .clickEvent(clickEvent(RUN_COMMAND, "/navi " + naviPoint.getX() + " " + naviPoint.getY() + " " + naviPoint.getZ()));

                player.sendMessage(component);
            });
        } else {
            String name = strings[1];
            boolean naviPointExists = naviPointService.naviPointExists(name, uuid);

            if (strings[0].equalsIgnoreCase("add")) {
                if (naviPointExists) {
                    player.sendMessage(text("This name is not available.", RED));
                } else {
                    naviPointService.addNaviPoint(name, player.getLocation(), uuid);
                    player.sendMessage(text("Added navi point.", GREEN));
                }
            } else if (strings[0].equalsIgnoreCase("remove")) {
                if (naviPointExists) {
                    naviPointService.deleteNaviPoint(name, uuid);
                    player.sendMessage(text("Removed navi point.", GREEN));
                } else {
                    player.sendMessage(text("This name is not available.", RED));
                }
            } else {
                sendUsage(player);
            }
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> options;
        switch (strings.length) {
            case 1 -> options = of("list", "add", "remove");
            case 2 -> options = of("[name]");
            default -> options = getOnlinePlayers().stream().map(Player::getName).toList();
        }
        return options;
    }

    private void sendUsage(Player player) {
        player.sendMessage(text("Use: /navipoint <list | add | remove> [name]", GRAY));
    }
}

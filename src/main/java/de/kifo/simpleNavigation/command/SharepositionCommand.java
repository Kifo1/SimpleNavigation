package de.kifo.simpleNavigation.command;

import com.google.inject.Inject;
import de.kifo.simpleNavigation.Main;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static de.kifo.simpleNavigation.Main.configuration;
import static de.kifo.simpleNavigation.Main.navigationService;
import static de.kifo.simpleNavigation.common.enums.NavigationType.BOSSBAR;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.UUID.randomUUID;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.event.ClickEvent.Action.RUN_COMMAND;
import static net.kyori.adventure.text.event.ClickEvent.clickEvent;
import static net.kyori.adventure.text.event.HoverEvent.Action.SHOW_TEXT;
import static net.kyori.adventure.text.event.HoverEvent.hoverEvent;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;
import static org.bukkit.Bukkit.getOnlinePlayers;
import static org.bukkit.Bukkit.getPlayer;
import static org.bukkit.Bukkit.getScheduler;

public class SharepositionCommand implements CommandExecutor, TabCompleter {

    @Inject
    private Main main;

    private Map<String, UUID> openShareRequests = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage("This command can only be used by players.");
            return false;
        }

        if (!player.hasPermission(configuration.getString("permission.command.shareposition"))) {
            player.sendMessage(text("You do not have permission to use this command.", RED));
            return false;
        }

        switch (strings.length) {
            case 1 -> {
                if (openShareRequests.containsKey(strings[0])) {
                    Player requester = getPlayer(openShareRequests.get(strings[0]));

                    if (nonNull(requester) && requester.isOnline()) {
                        navigationService.startPlayerNavigation(player, requester, BOSSBAR); //TODO Add ability to choose standard navigation type
                    } else {
                        player.sendMessage(text("The player needs to be online.", RED));
                    }

                    return true;
                }

                if (strings[0].length() == 36) {
                    player.sendMessage(text("The share request is no longer active.", RED));
                    return false;
                }

                Player target = getPlayer(strings[0]);
                if (isNull(target)) {
                    player.sendMessage(text("Player \"" + strings[0] + "\" couldn't be found.", RED));
                    return false;
                }

                String shareId = randomUUID().toString();
                openShareRequests.put(shareId, player.getUniqueId());
                getScheduler().runTaskLater(main, () -> openShareRequests.remove(shareId), 5 * 60 * 20L); //Remove request after 5 minutes

                Component component = text(player.getName() + " wants to share their position.", GOLD)
                        .appendSpace()
                        .append(text("Navigate to player.", YELLOW)
                                .hoverEvent(hoverEvent(SHOW_TEXT, text("Start navigation")))
                                .clickEvent(clickEvent(RUN_COMMAND, "/shareposition " + shareId)));

                target.sendMessage(component);
            }
            default -> sendUsage(player);
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return getOnlinePlayers().stream().map(p -> p.getName()).toList();
    }

    private void sendUsage(Player player) {
        player.sendMessage(text("Use: /sharepositon <player>", GRAY));
    }
}

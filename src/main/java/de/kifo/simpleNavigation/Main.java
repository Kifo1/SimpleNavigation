package de.kifo.simpleNavigation;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import de.kifo.simpleNavigation.command.ConsoleNaviCommand;
import de.kifo.simpleNavigation.command.NaviCommand;
import de.kifo.simpleNavigation.command.ReloadCommand;
import de.kifo.simpleNavigation.command.SettingsCommand;
import de.kifo.simpleNavigation.command.SharepositionCommand;
import de.kifo.simpleNavigation.common.files.Configuration;
import de.kifo.simpleNavigation.common.files.database.Database;
import de.kifo.simpleNavigation.common.service.ItemService;
import de.kifo.simpleNavigation.common.service.NavigationService;
import de.kifo.simpleNavigation.common.service.PlayerService;
import de.kifo.simpleNavigation.listener.NaviItemProtectListener;
import de.kifo.simpleNavigation.listener.PlayerJoinListener;
import de.kifo.simpleNavigation.listener.PlayerQuitListener;
import de.kifo.simpleNavigation.listener.gui.SettingInventoryListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import static com.google.inject.Guice.createInjector;
import static org.bukkit.Bukkit.getPluginManager;

@Getter
public final class Main extends JavaPlugin {

    private Injector injector;

    public static Configuration configuration;
    public static Database database;

    public static ItemService itemService;
    public static NavigationService navigationService;
    public static PlayerService playerService;

    @Override
    public void onLoad() {
        this.injector = createInjector(new RegistrationModule(this));

        configuration = new Configuration(this.getName(), "config");
        database = new Database(this.getName(), "data");
    }

    @Override
    public void onEnable() {
        itemService = new ItemService(this);
        navigationService = new NavigationService(this);
        playerService = new PlayerService(this);

        getCommand("consolenavi").setExecutor(injector.getInstance(ConsoleNaviCommand.class));
        getCommand("navi").setExecutor(injector.getInstance(NaviCommand.class));
        getCommand("reload").setExecutor(injector.getInstance(ReloadCommand.class));
        getCommand("settings").setExecutor(injector.getInstance(SettingsCommand.class));
        getCommand("shareposition").setExecutor(injector.getInstance(SharepositionCommand.class));

        getPluginManager().registerEvents(injector.getInstance(NaviItemProtectListener.class), this);
        getPluginManager().registerEvents(injector.getInstance(PlayerJoinListener.class), this);
        getPluginManager().registerEvents(injector.getInstance(PlayerQuitListener.class), this);
        getPluginManager().registerEvents(injector.getInstance(SettingInventoryListener.class), this);
    }

    @Override
    public void onDisable() {
        playerService.handleShutdown();

        database.closeConnection();
    }


    @AllArgsConstructor
    private class RegistrationModule extends AbstractModule {

        private Main main;

        @Override
        protected void configure() {
            bind(Main.class).toInstance(this.main);
        }
    }
}

package de.kifo.simpleNavigation;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import de.kifo.simpleNavigation.command.NaviCommand;
import de.kifo.simpleNavigation.common.files.Configuration;
import de.kifo.simpleNavigation.common.service.ItemService;
import de.kifo.simpleNavigation.common.service.NavigationService;
import de.kifo.simpleNavigation.listener.NaviItemProtectListener;
import lombok.AllArgsConstructor;
import org.bukkit.plugin.java.JavaPlugin;

import static com.google.inject.Guice.createInjector;
import static org.bukkit.Bukkit.getPluginManager;

public final class Main extends JavaPlugin {

    private Injector injector;
    private Configuration config;

    public static ItemService itemService;
    public static NavigationService navigationService;

    @Override
    public void onLoad() {
        this.injector = createInjector();

        this.config = new Configuration(this.getName(), "config");
    }

    @Override
    public void onEnable() {
        itemService = new ItemService(this);
        navigationService = new NavigationService(this);

        getCommand("navi").setExecutor(injector.getInstance(NaviCommand.class));

        getPluginManager().registerEvents(injector.getInstance(NaviItemProtectListener.class), this);
    }

    @Override
    public void onDisable() {

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

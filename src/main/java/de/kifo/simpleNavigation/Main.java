package de.kifo.simpleNavigation;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import lombok.AllArgsConstructor;
import org.bukkit.plugin.java.JavaPlugin;

import static com.google.inject.Guice.createInjector;

public final class Main extends JavaPlugin {

    private Injector injector;

    @Override
    public void onLoad() {
        this.injector = createInjector();
    }

    @Override
    public void onEnable() {

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

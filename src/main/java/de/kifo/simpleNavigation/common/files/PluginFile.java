package de.kifo.simpleNavigation.common.files;

import lombok.NonNull;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static java.util.Objects.isNull;
import static org.bukkit.configuration.file.YamlConfiguration.loadConfiguration;

public abstract class PluginFile {

    private final File file;
    private final File dir;
    private YamlConfiguration yamlConfiguration;

    public PluginFile(@NonNull String dirName, @NonNull String fileName) {
        this.dir = new File("./plugins/" + dirName);
        this.file = new File(dir, fileName + ".yml");
        this.yamlConfiguration = generateYamlConfiguration();
        this.loadStandardSettings();
    }

    private YamlConfiguration generateYamlConfiguration() {
        if (!dir.exists()) {
            dir.mkdirs();
        }

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return loadConfiguration(file);
    }

    private void loadStandardSettings() {
        HashMap<String, Object> settings = getStandardSettings();
        if (isNull(settings)) {
            return;
        }

        for (String key : settings.keySet()) {
            if (isNull(this.yamlConfiguration.get(key))) {
                this.yamlConfiguration.set(key, settings.get(key));
            }
        }

        this.save();
    }

    protected abstract HashMap<String, Object> getStandardSettings();

    public String getString(String key) {
        return yamlConfiguration.getString(key);
    }

    public List<String> getStringList(String key) {
        return yamlConfiguration.getStringList(key);
    }

    public boolean getBoolean(String key) {
        return yamlConfiguration.getBoolean(key);
    }

    public int getInt(String key) {
        return yamlConfiguration.getInt(key);
    }

    public long getLong(String key) {
        return yamlConfiguration.getLong(key);
    }

    public double getDouble(String key) {
        return yamlConfiguration.getDouble(key);
    }

    public void save() {
        try {
            yamlConfiguration.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void reloadConfig() {
        this.yamlConfiguration = loadConfiguration(file);
    }
}

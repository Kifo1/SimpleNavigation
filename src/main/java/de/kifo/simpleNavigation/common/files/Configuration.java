package de.kifo.simpleNavigation.common.files;

import lombok.NonNull;

import java.util.HashMap;

public class Configuration extends PluginFile {

    public Configuration(@NonNull String dirName, @NonNull String fileName) {
        super(dirName, fileName);
    }

    @Override
    protected HashMap<String, Object> getStandardSettings() {
        HashMap<String, Object> settings = new HashMap<>();

        // Permissions
        settings.put("permission.command.navi", "navi.use");
        settings.put("permission.command.reload", "navi.reload");

        // Settings
        settings.put("settings.message.distanceleft", true);

        return settings;
    }
}

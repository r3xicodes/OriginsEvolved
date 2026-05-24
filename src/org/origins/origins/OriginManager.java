package org.origins.origins;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OriginManager {
    private final JavaPlugin plugin;
    private Map<String, Origin> origins = new HashMap<>();

    public OriginManager(JavaPlugin plugin) {
        this.plugin = plugin;
        loadOrigins();
    }

    private void loadOrigins() {
        plugin.saveResource("origins.yml", false);
        FileConfiguration config = plugin.getConfig();
        // we can't assume origins are in main config; load separately
        FileConfiguration originsConfig = org.bukkit.configuration.file.YamlConfiguration.loadConfiguration(
                new java.io.File(plugin.getDataFolder(), "origins.yml"));
        for (String key : originsConfig.getKeys(false)) {
            String name = originsConfig.getString(key + ".name");
            String desc = originsConfig.getString(key + ".description");
            String primary = originsConfig.getString(key + ".primary");
            String secondary = originsConfig.getString(key + ".secondary");
            String crouch = originsConfig.getString(key + ".crouch");
            List<String> weaknesses = originsConfig.getStringList(key + ".weaknesses");
            String evolution = originsConfig.getString(key + ".evolution");
            org.bukkit.Material iconMat = org.bukkit.Material.PAPER;
            String iconStr = originsConfig.getString(key + ".icon");
            if (iconStr != null) {
                try {
                    iconMat = org.bukkit.Material.valueOf(iconStr.toUpperCase());
                } catch (IllegalArgumentException ignored) {}
            }
            Origin origin = new Origin(key, name, desc, primary, secondary, crouch, weaknesses, evolution, iconMat);
            origins.put(key, origin);
        }
    }

    public Origin getOrigin(String id) {
        return origins.get(id);
    }

    public Map<String, Origin> getAllOrigins() {
        return origins;
    }
}

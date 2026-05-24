package org.origins.evolution;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class EvolutionManager {
    private final JavaPlugin plugin;
    private Map<String, Evolution> evolutions = new HashMap<>();

    public EvolutionManager(JavaPlugin plugin) {
        this.plugin = plugin;
        loadEvolutions();
    }

    private void loadEvolutions() {
        plugin.saveResource("evolutions.yml", false);
        File file = new File(plugin.getDataFolder(), "evolutions.yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        for (String key : cfg.getKeys(false)) {
            String base = cfg.getString(key + ".base_origin");
            ConfigurationSection reqCfg = cfg.getConfigurationSection(key + ".requirements");
            Evolution e = new Evolution(key, base, reqCfg);
            evolutions.put(key, e);
        }
    }

    public Evolution getEvolution(String id) {
        return evolutions.get(id);
    }

    public Map<String, Evolution> getAllEvolutions() {
        return evolutions;
    }
}

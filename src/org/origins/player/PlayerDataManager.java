package org.origins.player;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class PlayerDataManager {
    private final JavaPlugin plugin;
    private File file;
    private FileConfiguration config;

    // simple memory cache
    private final HashMap<UUID, PlayerData> cache = new HashMap<>();

    public PlayerDataManager(JavaPlugin plugin) {
        this.plugin = plugin;
        createFile();
        load();
    }

    private void createFile() {
        file = new File(plugin.getDataFolder(), "playerdata.yml");
        if (!file.exists()) {
            plugin.saveResource("playerdata.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    private void load() {
        // load existing data into cache if needed
    }

    public PlayerData getData(UUID uuid) {
        return cache.computeIfAbsent(uuid, id -> {
            PlayerData data = new PlayerData(id);
            String path = id.toString();
            if (config.contains(path + ".origin")) {
                data.setOrigin(config.getString(path + ".origin"));
                data.setTimePlayedWithOrigin(config.getLong(path + ".time_played_with_origin", 0));
                data.setLastTransform(config.getLong(path + ".last_transform", 0));
                if (config.contains(path + ".boss_kills")) {
                    org.bukkit.configuration.ConfigurationSection secs = config.getConfigurationSection(path + ".boss_kills");
                    for (String k : secs.getKeys(false)) {
                        data.getBossKills().put(k, secs.getInt(k));
                    }
                }
                if (config.contains(path + ".evolution_progress")) {
                    data.getEvolutionProgress().putAll(config.getConfigurationSection(path + ".evolution_progress").getValues(false));
                }
                if (config.contains(path + ".states")) {
                    data.getStates().putAll(config.getConfigurationSection(path + ".states").getValues(false));
                }
                if (config.contains(path + ".ability_cooldowns")) {
                    config.getConfigurationSection(path + ".ability_cooldowns").getValues(false).forEach((k,v) -> {
                        data.setCooldown(k, ((Number)v).longValue());
                    });
                }
            }
            return data;
        });
    }

    public void saveAll() {
        // perform save in async task to avoid main-thread lag
        org.bukkit.scheduler.BukkitRunnable task = new org.bukkit.scheduler.BukkitRunnable() {
            public void run() {
                for (PlayerData data : cache.values()) {
                    data.save(config);
                }
                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        task.runTaskAsynchronously(plugin);
    }
}

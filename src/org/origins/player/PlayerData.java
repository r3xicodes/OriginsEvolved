package org.origins.player;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.UUID;

public class PlayerData {
    private final UUID uuid;
    private String origin = null;
    private long timePlayedWithOrigin = 0;
    private long lastTransform = 0;
    private HashMap<String, Integer> bossKills = new HashMap<>();
    private HashMap<String, Object> evolutionProgress = new HashMap<>();
    private HashMap<String, Object> states = new HashMap<>();
    private HashMap<String, Long> abilityCooldowns = new HashMap<>();

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
    }

    public void save(FileConfiguration config) {
        String path = uuid.toString();
        config.set(path + ".origin", origin);
        config.set(path + ".time_played_with_origin", timePlayedWithOrigin);
        config.set(path + ".last_transform", lastTransform);
        config.set(path + ".boss_kills", bossKills);
        config.set(path + ".evolution_progress", evolutionProgress);
        config.set(path + ".states", states);
        config.set(path + ".ability_cooldowns", abilityCooldowns);
    }

    // getters/setters omitted for brevity
    public String getOrigin() { return origin; }
    public void setOrigin(String origin) { this.origin = origin; }
    public long getTimePlayedWithOrigin() { return timePlayedWithOrigin; }
    public void addTime(long amount) { timePlayedWithOrigin += amount; }
    public long getLastTransform() { return lastTransform; }
    public void setLastTransform(long ts) { lastTransform = ts; }
    public void setTimePlayedWithOrigin(long t) { timePlayedWithOrigin = t; }
    public HashMap<String, Integer> getBossKills() { return bossKills; }
    public void incrementBossKill(String boss) { bossKills.put(boss, bossKills.getOrDefault(boss, 0) + 1); }
    public HashMap<String, Object> getEvolutionProgress() { return evolutionProgress; }
    public Object getState(String key) { return states.get(key); }
    public void setState(String key, Object value) { states.put(key, value); }

    /**
     * Helper for integer-valued state entries. Returns 0 if missing or not integer.
     */
    public int getIntState(String key) {
        Object v = states.get(key);
        return v instanceof Integer ? (Integer) v : 0;
    }
    public void setIntState(String key, int value) {
        states.put(key, value);
    }

    /**
     * Access to all stored state values (hydration, heat, etc.)
     */
    public HashMap<String, Object> getStates() { return states; }

    public long getCooldown(String ability) {
        return abilityCooldowns.getOrDefault(ability, 0L);
    }
    public void setCooldown(String ability, long timestamp) {
        abilityCooldowns.put(ability, timestamp);
    }
}

package org.origins.evolution;

import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

public class Evolution {
    private final String id;
    private final String baseOrigin;
    private final Map<String, Object> requirements = new HashMap<>();

    public Evolution(String id, String baseOrigin, ConfigurationSection reqSection) {
        this.id = id;
        this.baseOrigin = baseOrigin;
        if (reqSection != null) {
            for (String key : reqSection.getKeys(false)) {
                requirements.put(key, reqSection.get(key));
            }
        }
    }

    public String getId() { return id; }
    public String getBaseOrigin() { return baseOrigin; }
    public Map<String, Object> getRequirements() { return requirements; }
}

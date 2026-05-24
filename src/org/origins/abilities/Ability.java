package org.origins.abilities;

import org.bukkit.entity.Player;

public interface Ability {
    String getId();
    void onActivate(Player player);

    /**
     * Human‑readable name used for messages. Defaults to uppercase id with spaces.
     */
    default String getName() {
        return getId().replace('_', ' ').toUpperCase();
    }
}

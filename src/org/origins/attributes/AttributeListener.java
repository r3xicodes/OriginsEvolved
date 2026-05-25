package org.origins.attributes;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.entity.Player;
import org.origins.OriginsEvolved;
import org.origins.player.PlayerData;

public class AttributeListener implements Listener {
    private final OriginsEvolved plugin;
    private final AttributeManager manager;

    public AttributeListener(OriginsEvolved plugin, AttributeManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player p = (Player) e.getEntity();
        if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
            try {
                PlayerData d = plugin.getPlayerDataManager().getData(p.getUniqueId());
                String origin = d.getOrigin();
                if (origin == null) return;
                java.util.Map<String, java.util.Map<String, Double>> all = manager.getOriginAttributes();
                java.util.Map<String, Double> map = all.get(origin);
                if (map == null) return;
                if (map.containsKey("fall_damage_pct")) {
                    double pct = map.get("fall_damage_pct") / 100.0;
                    double original = e.getDamage();
                    double next = original * pct;
                    e.setDamage(next);
                    plugin.getLogger().info("Fall damage for " + p.getName() + " (origin=" + origin + "): " + original + " -> " + next);
                }
            } catch (Throwable ignored) {}
        }
    }
}

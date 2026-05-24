package org.origins.listeners;

import org.bukkit.entity.EnderDragon;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.origins.OriginsEvolved;
import org.origins.player.PlayerData;

public class BossKillListener implements Listener {
    private final OriginsEvolved plugin = OriginsEvolved.get();

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof EnderDragon) {
            if (event.getEntity().getKiller() != null) {
                PlayerData data = plugin.getPlayerDataManager().getData(event.getEntity().getKiller().getUniqueId());
                data.incrementBossKill("ender_dragon");
                // drop Dragon Orb
                if (plugin.getItemManager().getCustomItem("dragon_orb") != null) {
                    event.getEntity().getKiller().getInventory().addItem(plugin.getItemManager().getCustomItem("dragon_orb"));
                }
            }
        }
    }
}

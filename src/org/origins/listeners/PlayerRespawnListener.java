package org.origins.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.origins.OriginsEvolved;
import org.origins.player.PlayerData;

public class PlayerRespawnListener implements Listener {
    private final OriginsEvolved plugin = OriginsEvolved.get();

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        PlayerData data = plugin.getPlayerDataManager().getData(event.getPlayer().getUniqueId());
        String orig = data.getOrigin();
        if (orig == null) return;
        // reinitialize any origin-specific bars/states
        if ("merling".equals(orig)) {
            data.setIntState("hydration", 100);
            data.setIntState("dry_timer", 0);
        }
        if ("phantom".equals(orig)) {
            data.setIntState("sun_exposure", 0);
            data.setIntState("sun_damage_timer", 0);
        }
        if ("elytrian".equals(orig)) {
            data.setIntState("wing_charge", 0);
        }
        // clear boss bar on respawn so next tick will recreate properly
        org.origins.bossbar.BossBarManager.removeBar(event.getPlayer());
    }
}
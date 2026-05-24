package org.origins.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.origins.bossbar.BossBarManager;

public class BossBarListener implements Listener {
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        BossBarManager.removeBar(event.getPlayer());
    }
}
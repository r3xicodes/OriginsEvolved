package org.origins.attributes;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.entity.Player;
import org.origins.OriginsEvolved;

public class InventoryListener implements Listener {
    private final OriginsEvolved plugin;
    private final AttributeManager manager;

    public InventoryListener(OriginsEvolved plugin, AttributeManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    private void scheduleRefresh(Player p) {
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> manager.refreshToolEnchants(p), 2L);
    }

    @EventHandler
    public void onHeldChange(PlayerItemHeldEvent e) {
        scheduleRefresh(e.getPlayer());
    }

    @EventHandler
    public void onSwapHand(PlayerSwapHandItemsEvent e) {
        scheduleRefresh(e.getPlayer());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) scheduleRefresh((Player) e.getWhoClicked());
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {
        if (e.getWhoClicked() instanceof Player) scheduleRefresh((Player) e.getWhoClicked());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        scheduleRefresh(e.getPlayer());
    }
}

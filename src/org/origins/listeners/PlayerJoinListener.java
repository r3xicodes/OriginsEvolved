package org.origins.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.origins.OriginsEvolved;
import org.origins.player.PlayerData;

public class PlayerJoinListener implements Listener {
    private final OriginsEvolved plugin = OriginsEvolved.get();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        PlayerData data = plugin.getPlayerDataManager().getData(event.getPlayer().getUniqueId());
        if (data.getOrigin() == null) {
            event.getPlayer().sendMessage(ChatColor.AQUA + "Welcome! Choose an origin with /origin choose");
            event.getPlayer().sendMessage(ChatColor.AQUA + "Keybinds: press F for your primary ability, Shift+F for secondary, or just start sneaking to use a crouch ability (if you have one).");
            event.getPlayer().sendMessage(ChatColor.AQUA + "Caster items also work (use /origin givecaster) or run /origin ability.");
        }
    }
}

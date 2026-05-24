package org.origins.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffectType;
import org.origins.OriginsEvolved;
import org.origins.player.PlayerData;

public class PotionEffectListener implements Listener {
    private final OriginsEvolved plugin = OriginsEvolved.get();

    @EventHandler
    public void onPotionEffect(EntityPotionEffectEvent event) {
        if (event.getEntity() instanceof Player) {
            Player p = (Player) event.getEntity();
            PlayerData data = plugin.getPlayerDataManager().getData(p.getUniqueId());
            Boolean catalyst = (Boolean) data.getState("catalyst_active");
            if (catalyst != null && catalyst && event.getNewEffect() != null) {
                // simplify: increase amplifier by 1
                event.setCancelled(true);
                org.bukkit.potion.PotionEffect newEff = event.getNewEffect();
                int amp = newEff.getAmplifier() + 1;
                p.addPotionEffect(new org.bukkit.potion.PotionEffect(newEff.getType(), newEff.getDuration(), amp));
                data.setState("catalyst_active", false);
            }
        }
    }
}

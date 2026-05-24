package org.origins.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.origins.OriginsEvolved;
import org.origins.origins.Origin;
import org.origins.player.PlayerData;
import org.origins.abilities.AbilityUtil;

/**
 * Handles keybind-like events that the server can observe (hand swap and sneak toggle).
 *
 * F key is reported as a "swap hand" event.  We use that to fire primary/secondary
 * abilities according to sneaking state.  Sneak toggling on its own can trigger the
 * origin's crouch ability if configured.
 */
public class KeybindListener implements Listener {
    private final OriginsEvolved plugin = OriginsEvolved.get();

    @EventHandler
    public void onSwapHand(PlayerSwapHandItemsEvent event) {
        Player p = event.getPlayer();
        PlayerData data = plugin.getPlayerDataManager().getData(p.getUniqueId());
        String originId = data.getOrigin();
        if (originId == null) return;
        Origin orig = plugin.getOriginManager().getOrigin(originId);
        if (orig == null) return;
        // if the player is sneaking during swap, treat as secondary ability
        if (p.isSneaking()) {
            String abilityId = orig.getSecondaryAbility();
            if (abilityId != null && !abilityId.equals("none")) {
                plugin.getAbilityManager().activate(p, abilityId);
                event.setCancelled(true);
            }
        } else {
            String abilityId = orig.getPrimaryAbility();
            if (abilityId != null && !abilityId.equals("none")) {
                plugin.getAbilityManager().activate(p, abilityId);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onSneakToggle(PlayerToggleSneakEvent event) {
        Player p = event.getPlayer();
        PlayerData data = plugin.getPlayerDataManager().getData(p.getUniqueId());
        String originId = data.getOrigin();
        if (originId == null) return;
        Origin orig = plugin.getOriginManager().getOrigin(originId);
        if (orig == null) return;
        // bunny charge start/stop
        if (originId.equals("bunny")) {
            if (event.isSneaking() && p.isOnGround()) {
                data.setState("charge_active", true);
                data.setState("charge_level", 0);
                AbilityUtil.sendActionbar(p, "§eCharging jump...");
                p.playSound(p.getLocation(), org.bukkit.Sound.ENTITY_RABBIT_AMBIENT, 1f, 1.2f);
                p.getWorld().spawnParticle(org.bukkit.Particle.CRIT_MAGIC, p.getLocation().add(0,1,0), 15, 0.2,0.5,0.2,0.02);
            } else if (!event.isSneaking()) {
                data.setState("charge_active", false);
                p.playSound(p.getLocation(), org.bukkit.Sound.ENTITY_RABBIT_HURT, 1f, 0.8f);
            }
        }
        // crouch ability triggers when starting to sneak
        if (event.isSneaking()) {
            String abilityId = orig.getCrouchAbility();
            if (abilityId != null && !abilityId.equals("none")) {
                plugin.getAbilityManager().activate(p, abilityId);
            }
        }
    }
}
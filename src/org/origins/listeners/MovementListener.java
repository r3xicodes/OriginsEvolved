package org.origins.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.origins.OriginsEvolved;
import org.origins.player.PlayerData;

public class MovementListener implements Listener {
    private final OriginsEvolved plugin = OriginsEvolved.get();

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        PlayerData data = plugin.getPlayerDataManager().getData(p.getUniqueId());
        Boolean climbing = (Boolean) data.getState("climbing");
        if (climbing != null && climbing) {
            // if player is against wall and not on ground, allow upward motion
            if (!p.isOnGround() && !p.getLocation().getBlock().isPassable()) {
                p.setVelocity(p.getVelocity().setY(0.2));
            }
        }
        String origin = data.getOrigin();
        // other origins handled below (bunny, pawsworn, etc.)
        // bunny charging and aerial control
        if ("bunny".equals(origin)) {
            Boolean charging = (Boolean) data.getState("charge_active");
            Integer charge = (Integer) data.getState("charge_level");
            if (charge == null) charge = 0;
            if (charging != null && charging && p.isOnGround()) {
                charge = Math.min(100, charge + 2);
                data.setState("charge_level", charge);
                float prog = charge / 100f;
                org.origins.bossbar.BossBarManager.setBar(p, "Charge: " + charge + "%", prog, org.bukkit.boss.BarColor.GREEN);
            }
            // simple air control: small boost in facing direction when falling
            if (!p.isOnGround()) {
                org.bukkit.util.Vector dir = p.getLocation().getDirection();
                dir.setY(0);
                dir.normalize().multiply(0.1);
                p.setVelocity(p.getVelocity().add(dir));
            }
        }
        // pawsworn dash while sneaking
        if ("pawsworn".equals(origin)) {
            if (Boolean.TRUE.equals(data.getState("dash_active")) && p.isSneaking()) {
                org.bukkit.util.Vector dir = p.getLocation().getDirection().clone().multiply(0.3);
                dir.setY(0);
                p.setVelocity(p.getVelocity().add(dir));
            }
        }
        // werewolf leap when crouched in wolf form
        if ("werewolf".equals(origin)) {
            if (Boolean.TRUE.equals(data.getState("werewolf_form")) && p.isSneaking() && p.isOnGround()) {
                org.bukkit.util.Vector v = p.getLocation().getDirection().clone().multiply(0.8);
                v.setY(1.0);
                p.setVelocity(v);
            }
        }
    }
}

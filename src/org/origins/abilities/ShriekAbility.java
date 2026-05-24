package org.origins.abilities;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.origins.OriginsEvolved;
import org.origins.player.PlayerData;

public class ShriekAbility implements Ability {
    private static final long MAX_CHARGE_MS = 3000; // time to full charge

    @Override
    public String getId() {
        return "shriek";
    }

    @Override
    public void onActivate(Player player) {
        PlayerData data = OriginsEvolved.get().getPlayerDataManager().getData(player.getUniqueId());
        Object startObj = data.getState("shriek_charge_start");
        if (startObj instanceof Long) {
            // release shriek
            long start = (Long) startObj;
            long now = System.currentTimeMillis();
            float frac = Math.min(1f, (float)(now - start) / MAX_CHARGE_MS);
            data.setState("shriek_charge_start", null);
            org.origins.bossbar.BossBarManager.removeBar(player);
            performShriek(player, frac);
        } else {
            // start charging
            data.setState("shriek_charge_start", System.currentTimeMillis());
            player.sendActionBar("§5Charging shriek...");
            new BukkitRunnable() {
                @Override
                public void run() {
                    Object o = data.getState("shriek_charge_start");
                    if (!(o instanceof Long)) {
                        cancel();
                        return;
                    }
                    long s = (Long) o;
                    float f = Math.min(1f, (float)(System.currentTimeMillis() - s) / MAX_CHARGE_MS);
                    org.origins.bossbar.BossBarManager.setBar(player, "Shriek Charge", f, org.bukkit.boss.BarColor.PURPLE);
                    if (f >= 1f) {
                        cancel();
                    }
                }
            }.runTaskTimer(OriginsEvolved.get(), 0L, 5L);
        }
    }

    private void performShriek(Player player, float charge) {
        // visual/sound effects
        player.getWorld().spawnParticle(Particle.SONIC_BOOM, player.getLocation().add(0,1,0), 50, 1,1,1,0.1);
        player.playSound(player.getLocation(), Sound.ENTITY_WARDEN_SONIC_CHARGE, 2f, 1f);
        // calculate parameters
        double range = 5 + charge * 20;
        double dmg = 4 + charge * 6;
        Vector dir = player.getEyeLocation().getDirection();
        org.bukkit.util.RayTraceResult res = player.getWorld().rayTraceEntities(player.getEyeLocation(), dir, range, 0.5,
                e -> e instanceof LivingEntity && !e.equals(player));
        if (res != null) {
            Entity hit = res.getHitEntity();
            if (hit instanceof LivingEntity) {
                ((LivingEntity) hit).damage(dmg, player);
                ((LivingEntity) hit).addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.DARKNESS, 100, 0));
            }
        }
        player.sendMessage("§5Released shriek (" + (int)(charge*100) + "%)!");
    }
}

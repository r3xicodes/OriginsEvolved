package org.origins.abilities;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class VeilwalkAbility implements Ability {
    @Override
    public String getId() { return "veilwalk"; }

    @Override
    public void onActivate(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 60, 0));
        AbilityUtil.sendActionbar(player, "§7You step through the veil");
        player.getWorld().spawnParticle(org.bukkit.Particle.SMOKE_LARGE, player.getLocation(), 20, 0.5,0.5,0.5,0.01);
        player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_PHANTOM_FLAP, 1f, 1f);
    }
}
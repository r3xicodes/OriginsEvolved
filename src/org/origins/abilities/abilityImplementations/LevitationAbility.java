package org.origins.abilities.abilityImplementations;
import org.origins.abilities.*;

import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class LevitationAbility implements Ability {
    @Override
    public String getId() {
        return "levitation";
    }

    @Override
    public void onActivate(Player player) {
        AbilityUtil.sendActionbar(player, "§5Levitation!");
        AbilityUtil.showTitle(player, "§5Levitation", "", 5, 20, 5);
        player.sendMessage("§5Nearby hostiles levitate, allies slow fall.");
        for (Entity e : player.getNearbyEntities(6,6,6)) {
            if (e instanceof Player) {
                ((Player)e).addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 100, 0));
            } else if (e instanceof org.bukkit.entity.LivingEntity) {
                ((org.bukkit.entity.LivingEntity)e).addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 40, 1));
            }
        }
    }
}

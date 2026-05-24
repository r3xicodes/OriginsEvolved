package org.origins.abilities.abilityImplementations;
import org.origins.abilities.*;

import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class VerdantGraspAbility implements Ability {
    @Override
    public String getId() {
        return "verdant_grasp";
    }

    @Override
    public void onActivate(Player player) {
        AbilityUtil.sendActionbar(player, "§2Verdant Grasp!");
        AbilityUtil.showTitle(player, "§2Verdant Grasp", "", 5, 20, 5);
        player.sendMessage("§2Rising vines slow nearby foes.");
        for (Entity e : player.getNearbyEntities(5,5,5)) {
            if (e instanceof Player || e instanceof org.bukkit.entity.LivingEntity) {
                ((org.bukkit.entity.LivingEntity)e).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 1));
            }
        }
    }
}

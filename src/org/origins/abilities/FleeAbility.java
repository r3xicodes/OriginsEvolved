package org.origins.abilities;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FleeAbility implements Ability {
    @Override
    public String getId() {
        return "flee";
    }

    @Override
    public void onActivate(Player player) {
        AbilityUtil.sendActionbar(player, "§eYou flee! Speed boosted.");
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 120, 2));
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 120, 1));
    }
}
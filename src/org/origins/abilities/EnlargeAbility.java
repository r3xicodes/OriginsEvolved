package org.origins.abilities;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EnlargeAbility implements Ability {
    @Override
    public String getId() {
        return "enlarge";
    }

    @Override
    public void onActivate(Player player) {
        AbilityUtil.sendActionbar(player, "§2You feel larger!");
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 200, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 0));
    }
}
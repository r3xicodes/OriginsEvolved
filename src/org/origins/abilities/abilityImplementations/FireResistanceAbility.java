package org.origins.abilities.abilityImplementations;
import org.origins.abilities.*;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FireResistanceAbility implements Ability {
    @Override
    public String getId() {
        return "fire_resistance";
    }

    @Override
    public void onActivate(Player player) {
        AbilityUtil.sendActionbar(player, "§cFire Resistance activated!");
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 600, 0));
    }
}

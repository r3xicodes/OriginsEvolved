package org.origins.abilities.abilityImplementations;
import org.origins.abilities.*;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class RoarAbility implements Ability {
    @Override
    public String getId() {
        return "roar";
    }

    @Override
    public void onActivate(Player player) {
        AbilityUtil.sendActionbar(player, "§cROOOAR!");
        // apply slowness to nearby enemies
        player.getNearbyEntities(8,8,8).forEach(ent -> {
            if (ent instanceof Player && !ent.equals(player)) {
                ((Player) ent).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 1));
            }
        });
    }
}

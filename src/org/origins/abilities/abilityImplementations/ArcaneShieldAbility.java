package org.origins.abilities.abilityImplementations;
import org.origins.abilities.*;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.origins.abilities.AbilityUtil;

public class ArcaneShieldAbility implements Ability {
    @Override
    public String getId() {
        return "arcane_shield";
    }

    @Override
    public void onActivate(Player player) {
        AbilityUtil.sendActionbar(player, "§3Arcane Shield activated!");
        AbilityUtil.showTitle(player, "§3Arcane Shield", "", 5, 20, 5);
        player.sendMessage("§3You conjure an arcane shield!");
        player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 200, 1));
        player.getWorld().spawnParticle(org.bukkit.Particle.END_ROD, player.getLocation().add(0,1,0), 100, 0.5, 1, 0.5, 0.1);
    }
}

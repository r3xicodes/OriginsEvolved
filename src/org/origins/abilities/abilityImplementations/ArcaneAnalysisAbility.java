package org.origins.abilities.abilityImplementations;
import org.origins.abilities.*;

import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.origins.OriginsEvolved;
import org.origins.abilities.AbilityUtil;

public class ArcaneAnalysisAbility implements Ability {
    @Override
    public String getId() {
        return "arcane_analysis";
    }

    @Override
    public void onActivate(Player player) {
        AbilityUtil.sendActionbar(player, "§5Arcane Analysis engaged!");
        AbilityUtil.showTitle(player, "§5Arcane Analysis", "", 5, 20, 5);
        // play enchant table particles
        player.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, player.getLocation().add(0,1,0), 50, 0.5, 0.5, 0.5, 0.1);
        // give some xp as reward
        player.giveExp(5);
        // cooldown handled externally
    }
}

package org.origins.abilities.abilityImplementations;
import org.origins.abilities.*;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class BoostAbility implements Ability {
    @Override
    public String getId() {
        return "boost";
    }

    @Override
    public void onActivate(Player player) {
        if (player.isGliding()) {
            AbilityUtil.sendActionbar(player, "§eFlight Boost!");
            Vector direction = player.getLocation().getDirection();
            Vector boost = direction.multiply(1.5).setY(direction.getY() + 0.5);
            player.setVelocity(player.getVelocity().add(boost));
            player.getWorld().spawnParticle(org.bukkit.Particle.CLOUD, player.getLocation(), 20, 0.5, 0.5, 0.5, 0.1);
            player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1f, 1.2f);
        } else {
            AbilityUtil.sendActionbar(player, "§eYou must be gliding to boost!");
        }
    }
}

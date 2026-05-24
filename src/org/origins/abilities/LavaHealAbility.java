package org.origins.abilities;

import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.origins.abilities.AbilityUtil;

public class LavaHealAbility implements Ability {
    @Override
    public String getId() {
        return "lava_heal";
    }

    @Override
    public void onActivate(Player player) {
        if (player.getLocation().getBlock().getType() == Material.LAVA) {
            double heal = Math.min(player.getMaxHealth() - player.getHealth(), 4.0);
            player.setHealth(player.getHealth() + heal);
            AbilityUtil.sendActionbar(player, "§cLava heals you for " + heal + " HP!");
            player.getWorld().spawnParticle(org.bukkit.Particle.FLAME, player.getLocation().add(0,1,0), 20, 0.5, 0.5, 0.5, 0.1);
            player.playSound(player.getLocation(), org.bukkit.Sound.BLOCK_LAVA_POP, 1f, 1f);
        } else {
            AbilityUtil.sendActionbar(player, "§cYou must be in lava to heal!");
        }
    }
}
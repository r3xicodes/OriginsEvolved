package org.origins.abilities;

import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class ProspectingAbility implements Ability {
    @Override
    public String getId() {
        return "prospecting";
    }

    @Override
    public void onActivate(Player player) {
        AbilityUtil.sendActionbar(player, "§6Prospecting");
        AbilityUtil.showTitle(player, "§6Prospecting", "", 5, 20, 5);
        player.sendMessage("§6Ore highlighted nearby.");
        // display particles over nearby ore blocks
        for (int dx=-10; dx<=10; dx++) {
            for (int dy=-5; dy<=5; dy++) {
                for (int dz=-10; dz<=10; dz++) {
                    Location loc = player.getLocation().add(dx, dy, dz);
                    Block b = loc.getBlock();
                    Material m = b.getType();
                    if (m == Material.IRON_ORE || m == Material.GOLD_ORE || m == Material.DIAMOND_ORE || m == Material.COAL_ORE) {
                        player.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, loc.add(0.5,0.5,0.5), 1, 0,0,0,0);
                    }
                }
            }
        }
    }
}
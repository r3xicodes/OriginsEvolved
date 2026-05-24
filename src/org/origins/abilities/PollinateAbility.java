package org.origins.abilities;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.origins.abilities.AbilityUtil;

public class PollinateAbility implements Ability {
    @Override
    public String getId() {
        return "pollinate";
    }

    @Override
    public void onActivate(Player player) {
        AbilityUtil.sendActionbar(player, "§aPollination begins!");
        AbilityUtil.showTitle(player, "§aPollination", "", 5, 20, 5);
        // simulate bonemeal effect near player
        player.getWorld().spawnParticle(org.bukkit.Particle.VILLAGER_HAPPY, player.getLocation(), 50, 2, 1, 2);
        // give a few honey items
        player.getInventory().addItem(new ItemStack(Material.HONEY_BOTTLE, 2));
    }
}
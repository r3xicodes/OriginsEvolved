package org.origins.abilities.abilityImplementations;
import org.origins.abilities.*;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ProspectingAbility implements Ability {
    private static final Set<Material> ORE_BLOCKS = Set.of(
        Material.COAL_ORE,
        Material.IRON_ORE,
        Material.COPPER_ORE,
        Material.GOLD_ORE,
        Material.REDSTONE_ORE,
        Material.LAPIS_ORE,
        Material.EMERALD_ORE,
        Material.DIAMOND_ORE,
        Material.ANCIENT_DEBRIS,
        Material.NETHER_GOLD_ORE,
        Material.NETHER_QUARTZ_ORE,
        Material.DEEPSLATE_COAL_ORE,
        Material.DEEPSLATE_COPPER_ORE,
        Material.DEEPSLATE_IRON_ORE,
        Material.DEEPSLATE_GOLD_ORE,
        Material.DEEPSLATE_REDSTONE_ORE,
        Material.DEEPSLATE_LAPIS_ORE,
        Material.DEEPSLATE_EMERALD_ORE,
        Material.DEEPSLATE_DIAMOND_ORE
    );

    @Override
    public String getId() {
        return "prospecting";
    }

    @Override
    public void onActivate(Player player) {
        AbilityUtil.sendActionbar(player, "§6Prospecting — ore blocks are visible through walls for 10s");
        AbilityUtil.showTitle(player, "§6Prospecting", "§7Ores are revealed", 5, 20, 5);
        player.sendMessage("§6Ore blocks are highlighted for 10 seconds.");

        Location base = player.getLocation();
        int radius = 24; // roughly 1.5 chunks
        int vertical = 12;
        double radiusSq = radius * radius;
        Map<Location, BlockData> originalBlockData = new HashMap<>();

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -vertical; dy <= vertical; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    if (dx * dx + dy * dy + dz * dz > radiusSq) continue;
                    Location loc = base.clone().add(dx, dy, dz);
                    Block block = loc.getBlock();
                    Material type = block.getType();
                    if (!ORE_BLOCKS.contains(type)) continue;

                    originalBlockData.put(loc, block.getBlockData());
                    player.sendBlockChange(loc, block.getBlockData());
                }
            }
        }

        if (originalBlockData.isEmpty()) {
            player.sendMessage("§eNo ore blocks found in range.");
            return;
        }

        new BukkitRunnable() {
            public void run() {
                if (!player.isOnline()) {
                    cancel();
                    return;
                }
                for (Map.Entry<Location, BlockData> entry : originalBlockData.entrySet()) {
                    player.sendBlockChange(entry.getKey(), entry.getValue());
                }
                cancel();
            }
        }.runTaskLater(org.origins.OriginsEvolved.get(), 20L * 10);
    }
}

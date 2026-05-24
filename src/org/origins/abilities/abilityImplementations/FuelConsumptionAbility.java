package org.origins.abilities.abilityImplementations;
import org.origins.abilities.*;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.origins.OriginsEvolved;
import org.origins.player.PlayerData;
import org.origins.abilities.AbilityUtil;

public class FuelConsumptionAbility implements Ability {
    @Override
    public String getId() {
        return "fuel_consumption";
    }

    @Override
    public void onActivate(Player player) {
        PlayerData data = OriginsEvolved.get().getPlayerDataManager().getData(player.getUniqueId());
        Integer heat = (Integer) data.getState("heat_bar");
        if (heat == null) heat = 0;

        // Find and consume fuel item
        ItemStack fuel = findFuelItem(player);
        if (fuel == null) {
            AbilityUtil.sendActionbar(player, "§cNo fuel items in inventory!");
            return;
        }

        int heatGain = getHeatValue(fuel.getType());
        heat += heatGain;
        data.setState("heat_bar", heat);

        // Remove one item
        fuel.setAmount(fuel.getAmount() - 1);

        // Display heat on boss bar
        float progress = Math.min(1f, heat / 10000f);
        org.origins.bossbar.BossBarManager.setBar(player, "Heat: " + heat + "°C", progress, org.bukkit.boss.BarColor.RED);
        AbilityUtil.sendActionbar(player, "§4Consumed fuel! Heat: " + heat + "°C");
        player.sendMessage("§4Consumed fuel! Heat: " + heat + "°C");
    }

    private ItemStack findFuelItem(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && isFuel(item.getType())) {
                return item;
            }
        }
        return null;
    }

    private boolean isFuel(Material material) {
        return material == Material.COAL ||
               material == Material.CHARCOAL ||
               material == Material.STICK ||
               material == Material.BLAZE_ROD ||
               material == Material.LAVA_BUCKET ||
               material == Material.COAL_BLOCK ||
               material == Material.DRIED_KELP_BLOCK ||
               material == Material.BAMBOO ||
               material == Material.STRIPPED_BAMBOO_BLOCK ||
               material == Material.BAMBOO_BLOCK ||
               material == Material.KELP ||
               material == Material.DRIED_KELP ||
               material == Material.SEAGRASS ||
               material == Material.SEA_PICKLE ||
               material == Material.WHEAT ||
               material == Material.CARROT ||
               material == Material.POTATO ||
               material == Material.BEETROOT ||
               material == Material.PUMPKIN ||
               material == Material.MELON ||
               material == Material.APPLE ||
               material == Material.SUGAR_CANE ||
               material == Material.BREAD ||
               material == Material.COOKIE ||
               material == Material.CAKE ||
               material == Material.PUMPKIN_PIE ||
               material == Material.GOLDEN_APPLE ||
               material == Material.ENCHANTED_GOLDEN_APPLE ||
               material == Material.GOLDEN_CARROT ||
               material == Material.BAKED_POTATO ||
               material == Material.POISONOUS_POTATO ||
               material == Material.BEETROOT_SOUP ||
               material == Material.MUSHROOM_STEW ||
               material == Material.RABBIT_STEW ||
               material == Material.SUSPICIOUS_STEW ||
               material == Material.COOKED_BEEF ||
               material == Material.COOKED_PORKCHOP ||
               material == Material.COOKED_MUTTON ||
               material == Material.COOKED_CHICKEN ||
               material == Material.COOKED_RABBIT ||
               material == Material.COOKED_COD ||
               material == Material.COOKED_SALMON ||
               material == Material.COOKED_MUTTON ||
               material == Material.BEEF ||
               material == Material.PORKCHOP ||
               material == Material.MUTTON ||
               material == Material.CHICKEN ||
               material == Material.RABBIT ||
               material == Material.COD ||
               material == Material.SALMON ||
               material == Material.TROPICAL_FISH ||
               material == Material.PUFFERFISH;
    }

    private int getHeatValue(Material material) {
        switch (material) {
            case COAL: return 1600;
            case CHARCOAL: return 1600;
            case COAL_BLOCK: return 16000;
            case BLAZE_ROD: return 2400;
            case LAVA_BUCKET: return 20000;
            case STICK: return 100;
            case BAMBOO: return 50;
            case DRIED_KELP: return 400;
            case DRIED_KELP_BLOCK: return 4000;
            case SEA_PICKLE: return 200;
            case KELP: return 300;
            case SEAGRASS: return 100;
            case WHEAT: return 100;
            case CARROT: return 200;
            case POTATO: return 200;
            case BEETROOT: return 200;
            case PUMPKIN: return 300;
            case MELON: return 300;
            case APPLE: return 400;
            case SUGAR_CANE: return 50;
            case BREAD: return 600;
            case COOKIE: return 200;
            case CAKE: return 800;
            case PUMPKIN_PIE: return 600;
            case GOLDEN_APPLE: return 2000;
            case ENCHANTED_GOLDEN_APPLE: return 10000;
            case GOLDEN_CARROT: return 1000;
            case BAKED_POTATO: return 300;
            case POISONOUS_POTATO: return 100;
            case BEETROOT_SOUP: return 400;
            case MUSHROOM_STEW: return 400;
            case RABBIT_STEW: return 600;
            case SUSPICIOUS_STEW: return 400;
            case COOKED_BEEF: return 600;
            case COOKED_PORKCHOP: return 600;
            case COOKED_MUTTON: return 600;
            case COOKED_CHICKEN: return 600;
            case COOKED_RABBIT: return 600;
            case COOKED_COD: return 400;
            case COOKED_SALMON: return 400;
            case BEEF: return 300;
            case PORKCHOP: return 300;
            case MUTTON: return 300;
            case CHICKEN: return 300;
            case RABBIT: return 300;
            case COD: return 200;
            case SALMON: return 200;
            case TROPICAL_FISH: return 200;
            case PUFFERFISH: return 200;
            default: return 100;
        }
    }
}

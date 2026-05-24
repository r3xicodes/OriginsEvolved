package org.origins.abilities;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class HiveCallAbility implements Ability {
    @Override
    public String getId() {
        return "hive_call";
    }

    @Override
    public void onActivate(Player player) {
        AbilityUtil.sendActionbar(player, "§eHive Call!");
        AbilityUtil.showTitle(player, "§eHive Call", "", 5, 20, 5);
        player.sendMessage("§eNearby ants are strengthened.");
        for (Player p : player.getWorld().getPlayers()) {
            if (p.getLocation().distance(player.getLocation()) < 6) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 0));
                p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 200, 0));
            }
        }
    }
}
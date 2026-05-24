package org.origins.abilities.abilityImplementations;
import org.origins.abilities.*;

import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class HowlAbility implements Ability {
    @Override
    public String getId() {
        return "howl";
    }

    @Override
    public void onActivate(Player player) {
        AbilityUtil.sendActionbar(player, "§4Howl!");
        AbilityUtil.showTitle(player, "§4Howl", "", 5, 20, 5);
        player.sendMessage("§4You let out a fearsome howl.");
        // buff nearby wolves and players
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 200, 0));
        for (org.bukkit.entity.Entity e : player.getNearbyEntities(8,8,8)) {
            if (e instanceof Wolf || e instanceof Player) {
                ((org.bukkit.entity.LivingEntity)e).addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 200, 0));
            }
        }
    }
}

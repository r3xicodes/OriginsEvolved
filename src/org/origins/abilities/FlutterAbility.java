package org.origins.abilities;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FlutterAbility implements Ability {
    @Override
    public String getId() {
        return "flutter";
    }

    @Override
    public void onActivate(Player player) {
        AbilityUtil.sendActionbar(player, "§fYou flutter gracefully!");
        // give slow fall for a short time
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 100, 0));
    }
}
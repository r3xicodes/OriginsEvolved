package org.origins.abilities;

import org.bukkit.entity.Player;

public class GlideAbility implements Ability {
    @Override
    public String getId() {
        return "glide";
    }

    @Override
    public void onActivate(Player player) {
        AbilityUtil.sendActionbar(player, "§bGlide activated!");
        AbilityUtil.showTitle(player, "§bGlide", "", 5, 20, 5);
        // give slow falling effect
        player.addPotionEffect(new org.bukkit.potion.PotionEffect(
                org.bukkit.potion.PotionEffectType.SLOW_FALLING, 200, 0));
    }
}

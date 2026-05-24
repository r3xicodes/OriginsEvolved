package org.origins.abilities;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class TenaciousAbility implements Ability {
    @Override
    public String getId() {
        return "tenacious";
    }

    @Override
    public void onActivate(Player player) {
        AbilityUtil.sendActionbar(player, "§aTenacious!");
        AbilityUtil.showTitle(player, "§aTenacious", "", 5, 20, 5);
        player.sendMessage("§aYou feel undead resilience.");
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 400, 1));
    }
}
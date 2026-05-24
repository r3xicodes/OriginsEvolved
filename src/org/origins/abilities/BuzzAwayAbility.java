package org.origins.abilities;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.origins.abilities.AbilityUtil;

public class BuzzAwayAbility implements Ability {
    @Override
    public String getId() {
        return "buzz_away";
    }

    @Override
    public void onActivate(Player player) {
        AbilityUtil.sendActionbar(player, "§bBuzzing away!");
        AbilityUtil.showTitle(player, "§bBuzz", "", 5, 20, 5);
        player.sendMessage("§bBuzzing away with levitation!");
        player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 60, 1));
    }
}
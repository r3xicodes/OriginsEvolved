package org.origins.abilities.abilityImplementations;
import org.origins.abilities.*;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class WingedAscensionAbility implements Ability {
    @Override
    public String getId() {
        return "winged_ascension";
    }

    @Override
    public void onActivate(Player player) {
        AbilityUtil.sendActionbar(player, "§bWinged Ascension!");
        AbilityUtil.showTitle(player, "§bWinged Ascension", "", 5, 20, 5);
        player.sendMessage("§bYour spectral wings unfurl.");
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 160, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 160, 0));
    }
}

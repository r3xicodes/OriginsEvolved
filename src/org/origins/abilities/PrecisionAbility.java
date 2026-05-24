package org.origins.abilities;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PrecisionAbility implements Ability {
    @Override
    public String getId() {
        return "precision";
    }

    @Override
    public void onActivate(Player player) {
        AbilityUtil.sendActionbar(player, "§fPrecision mode!");
        // give invisibility briefly
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100, 0));
    }
}
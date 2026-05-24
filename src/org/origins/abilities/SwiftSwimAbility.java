package org.origins.abilities;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SwiftSwimAbility implements Ability {
    @Override
    public String getId() { return "swift_swim"; }

    @Override
    public void onActivate(Player player) {
        AbilityUtil.sendActionbar(player, "§bSwift swim!");
        player.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 200, 0));
    }
}
package org.origins.abilities.abilityImplementations;
import org.origins.abilities.*;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class StalkAbility implements Ability {
    @Override
    public String getId() {
        return "stalk";
    }

    @Override
    public void onActivate(Player player) {
        AbilityUtil.sendActionbar(player, "§8You stalk silently.");
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100, 0));
    }
}

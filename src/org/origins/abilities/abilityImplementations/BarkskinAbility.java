package org.origins.abilities.abilityImplementations;
import org.origins.abilities.*;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BarkskinAbility implements Ability {
    @Override
    public String getId() {
        return "barkskin";
    }

    @Override
    public void onActivate(Player player) {
        AbilityUtil.sendActionbar(player, "§aBarkskin activated!");
        AbilityUtil.showTitle(player, "§aBarkskin", "", 5, 20, 5);
        player.sendMessage("§aYour skin hardens like bark.");
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 400, 0));
    }
}

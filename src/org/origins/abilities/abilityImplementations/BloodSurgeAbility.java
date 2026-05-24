package org.origins.abilities.abilityImplementations;
import org.origins.abilities.*;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BloodSurgeAbility implements Ability {
    @Override
    public String getId() {
        return "blood_surge";
    }

    @Override
    public void onActivate(Player player) {
        AbilityUtil.sendActionbar(player, "§4Blood Surge!");
        AbilityUtil.showTitle(player, "§4Blood Surge", "", 5, 20, 5);
        player.sendMessage("§4Your cursed blood stabilizes.");
        player.removePotionEffect(PotionEffectType.POISON);
        player.removePotionEffect(PotionEffectType.WITHER);
        player.removePotionEffect(PotionEffectType.SLOW);
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 200, 0));
    }
}

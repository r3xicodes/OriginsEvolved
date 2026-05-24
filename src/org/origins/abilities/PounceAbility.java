package org.origins.abilities;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class PounceAbility implements Ability {
    @Override
    public String getId() {
        return "pounce";
    }

    @Override
    public void onActivate(Player player) {
        AbilityUtil.sendActionbar(player, "§6Pounce!");
        AbilityUtil.showTitle(player, "§6Pounce", "", 5, 20, 5);
        player.sendMessage("§6You leap forward with a pounce!");
        Vector vel = player.getLocation().getDirection().clone().multiply(1.2);
        vel.setY(0.5);
        player.setVelocity(vel);
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100, 0));
    }
}
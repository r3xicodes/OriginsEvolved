package org.origins.abilities;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DraconicLeapAbility implements Ability {
    @Override
    public String getId() {
        return "draconic_leap";
    }

    @Override
    public void onActivate(Player player) {
        AbilityUtil.sendActionbar(player, "§4Draconic Leap!");
        AbilityUtil.showTitle(player, "§4Draconic Leap", "", 5, 20, 5);
        player.sendMessage("§4You charge forward with draconic power.");
        Vector v = player.getLocation().getDirection().multiply(1.5);
        v.setY(0.6);
        player.setVelocity(v);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 100, 0));
    }
}
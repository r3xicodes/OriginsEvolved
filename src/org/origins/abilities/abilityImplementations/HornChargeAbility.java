package org.origins.abilities.abilityImplementations;
import org.origins.abilities.*;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class HornChargeAbility implements Ability {
    @Override
    public String getId() {
        return "horn_charge";
    }

    @Override
    public void onActivate(Player player) {
        AbilityUtil.sendActionbar(player, "§6Horn Charge!");
        AbilityUtil.showTitle(player, "§6Horn Charge", "", 5, 20, 5);
        player.sendMessage("§6You lower your horns and rush ahead.");
        Vector v = player.getLocation().getDirection().multiply(1.2);
        v.setY(0.3);
        player.setVelocity(v);
    }
}

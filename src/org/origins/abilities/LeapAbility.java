package org.origins.abilities;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class LeapAbility implements Ability {
    @Override
    public String getId() {
        return "leap";
    }

    @Override
    public void onActivate(Player player) {
        AbilityUtil.sendActionbar(player, "§aLeap!");
        AbilityUtil.showTitle(player, "§aLeap", "", 5, 20, 5);
        player.sendMessage("§aYou leap high into the air.");
        Vector v = player.getLocation().getDirection().multiply(0.8);
        v.setY(1.0);
        player.setVelocity(v);
    }
}
package org.origins.abilities;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class WindLaunchAbility implements Ability {
    @Override
    public String getId() {
        return "wind_launch";
    }

    @Override
    public void onActivate(Player player) {
        AbilityUtil.sendActionbar(player, "§3Wind Launch!");
        AbilityUtil.showTitle(player, "§3Wind Launch", "", 5, 20, 5);
        player.sendMessage("§3A burst of air propels you.");
        Vector vel = player.getLocation().getDirection().multiply(1.5);
        vel.setY(0.5);
        player.setVelocity(vel);
    }
}
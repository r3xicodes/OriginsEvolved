package org.origins.abilities;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class DistortionDodgeAbility implements Ability {
    @Override
    public String getId() {
        return "distortion_dodge";
    }

    @Override
    public void onActivate(Player player) {
        Location loc = player.getLocation();
        // random small teleport
        double dx = (Math.random() - 0.5) * 4;
        double dz = (Math.random() - 0.5) * 4;
        Location dest = loc.clone().add(dx, 0, dz);
        player.teleport(dest);
        AbilityUtil.sendActionbar(player, "§5Distorted dodge!");
    }
}
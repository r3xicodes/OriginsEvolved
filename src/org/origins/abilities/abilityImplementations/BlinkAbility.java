package org.origins.abilities.abilityImplementations;
import org.origins.abilities.*;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class BlinkAbility implements Ability {
    @Override
    public String getId() {
        return "blink";
    }

    @Override
    public void onActivate(Player player) {
        Location loc = player.getLocation();
        Location dest = loc.add(loc.getDirection().multiply(5));
        dest.setY(loc.getY());
        player.teleport(dest);
        AbilityUtil.sendActionbar(player, "§dBlink!");
    }
}

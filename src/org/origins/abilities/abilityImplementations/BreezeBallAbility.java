package org.origins.abilities.abilityImplementations;
import org.origins.abilities.*;

import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.util.Vector;

public class BreezeBallAbility implements Ability {
    @Override
    public String getId() {
        return "breeze_ball";
    }

    @Override
    public void onActivate(Player player) {
        AbilityUtil.sendActionbar(player, "§bBreeze Ball!");
        AbilityUtil.showTitle(player, "§bBreeze Ball", "", 5, 20, 5);
        player.sendMessage("§bYou launch a ball of wind.");
        Snowball ball = player.launchProjectile(Snowball.class);
        ball.setVelocity(player.getLocation().getDirection().multiply(1.5));
    }
}

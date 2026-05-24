package org.origins.abilities.abilityImplementations;
import org.origins.abilities.*;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.origins.OriginsEvolved;
import org.origins.player.PlayerData;

public class ChargeJumpAbility implements Ability {
    @Override
    public String getId() { return "charge_jump"; }

    @Override
    public void onActivate(Player player) {
        PlayerData data = OriginsEvolved.get().getPlayerDataManager().getData(player.getUniqueId());
        Integer charge = (Integer) data.getState("charge_level");
        if (charge == null) charge = 0;
        // simple jump boost proportional to charge
        Vector vel = player.getVelocity();
        vel.setY(0.5 + charge / 200.0);
        player.setVelocity(vel);
        data.setState("charge_level", 0);
        data.setState("charge_active", false);
        AbilityUtil.sendActionbar(player, "§eCharge released! Jump power " + charge);
        player.getWorld().spawnParticle(org.bukkit.Particle.FLAME, player.getLocation().add(0,1,0), 30, 0.3,0.3,0.3,0.05);
        player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_RABBIT_HURT, 1f, 1f);
    }
}

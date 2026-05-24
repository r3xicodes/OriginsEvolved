package org.origins.abilities.abilityImplementations;
import org.origins.abilities.*;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.origins.OriginsEvolved;
import org.origins.player.PlayerData;

public class MegaJumpAbility implements Ability {
    @Override
    public String getId() { return "mega_jump"; }

    @Override
    public void onActivate(Player player) {
        PlayerData data = OriginsEvolved.get().getPlayerDataManager().getData(player.getUniqueId());
        Integer charge = (Integer) data.getState("charge_level");
        if (charge == null) charge = 0;
        Vector vel = player.getVelocity();
        vel.setY(1.0 + charge / 100.0);
        player.setVelocity(vel);
        data.setState("charge_level", 0);
        data.setState("charge_active", false);
        AbilityUtil.sendActionbar(player, "§cMega jump! Power " + charge);
    }
}

package org.origins.abilities.abilityImplementations;
import org.origins.abilities.*;

import org.bukkit.entity.Player;

public class InfectiousBiteAbility implements Ability {
    @Override
    public String getId() {
        return "infectious_bite";
    }

    @Override
    public void onActivate(Player player) {
        AbilityUtil.sendActionbar(player, "§8Infectious Bite");
        AbilityUtil.showTitle(player, "§8Infectious Bite", "", 5, 20, 5);
        player.sendMessage("§8Your next attack will poison the target.");
        // lore: actual effect should be applied in EntityDamageListener in the future
    }
}

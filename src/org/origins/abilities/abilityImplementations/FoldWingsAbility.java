package org.origins.abilities.abilityImplementations;
import org.origins.abilities.*;

import org.bukkit.entity.Player;

public class FoldWingsAbility implements Ability {
    @Override
    public String getId() {
        return "fold_wings";
    }

    @Override
    public void onActivate(Player player) {
        AbilityUtil.sendActionbar(player, "§bWings folded.");
        player.sendMessage("Your wings have been folded.");
        // this ability is mostly informational for now
    }
}

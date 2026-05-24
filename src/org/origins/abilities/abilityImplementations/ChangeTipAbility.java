package org.origins.abilities.abilityImplementations;
import org.origins.abilities.*;

import org.bukkit.entity.Player;

public class ChangeTipAbility implements Ability {
    @Override
    public String getId() {
        return "change_tip";
    }

    @Override
    public void onActivate(Player player) {
        // placeholder: just notify
        AbilityUtil.sendActionbar(player, "§3Arrow tip changed!");
        player.sendMessage("Your arrow tip has been changed (placeholder).");
    }
}

package org.origins.abilities.abilityImplementations;
import org.origins.abilities.*;

import org.bukkit.entity.Player;
import org.origins.OriginsEvolved;
import org.origins.player.PlayerData;

public class DashAbility implements Ability {
    @Override
    public String getId() {
        return "dash";
    }

    @Override
    public void onActivate(Player player) {
        PlayerData data = OriginsEvolved.get().getPlayerDataManager().getData(player.getUniqueId());
        Boolean active = (Boolean) data.getState("dash_active");
        if (active != null && active) {
            data.setState("dash_active", false);
            AbilityUtil.sendActionbar(player, "§cDash deactivated.");
            player.sendMessage("§cDash deactivated.");
        } else {
            data.setState("dash_active", true);
            AbilityUtil.sendActionbar(player, "§aDash activated!");
            player.sendMessage("§aYou can now dash while crouching.");
        }
    }
}

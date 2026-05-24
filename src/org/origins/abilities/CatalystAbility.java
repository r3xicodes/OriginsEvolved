package org.origins.abilities;

import org.bukkit.entity.Player;
import org.origins.OriginsEvolved;
import org.origins.player.PlayerData;
import org.origins.abilities.AbilityUtil;

public class CatalystAbility implements Ability {
    @Override
    public String getId() {
        return "catalyst";
    }

    @Override
    public void onActivate(Player player) {
        PlayerData data = OriginsEvolved.get().getPlayerDataManager().getData(player.getUniqueId());
        Boolean active = (Boolean) data.getState("catalyst_active");
        if (active != null && active) {
            data.setState("catalyst_active", false);
            AbilityUtil.sendActionbar(player, "§6Catalyst deactivated.");
            player.sendMessage("§6Catalyst deactivated.");
        } else {
            data.setState("catalyst_active", true);
            AbilityUtil.sendActionbar(player, "§6Catalyst activated!");
            player.sendMessage("§6Catalyst activated. Your next potions will be modified.");
        }
    }
}

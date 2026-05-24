package org.origins.abilities;

import org.bukkit.entity.Player;
import org.origins.OriginsEvolved;
import org.origins.player.PlayerData;
import org.origins.abilities.AbilityUtil;

public class ToggleClimbAbility implements Ability {
    @Override
    public String getId() {
        return "toggle_climb";
    }

    @Override
    public void onActivate(Player player) {
        PlayerData data = OriginsEvolved.get().getPlayerDataManager().getData(player.getUniqueId());
        Boolean climbing = (Boolean) data.getState("climbing");
        if (climbing != null && climbing) {
            data.setState("climbing", false);
            AbilityUtil.sendActionbar(player, "§7Climbing disabled.");
            player.sendMessage("§7Climbing disabled.");
        } else {
            data.setState("climbing", true);
            AbilityUtil.sendActionbar(player, "§7Climbing enabled!");
            player.sendMessage("§7Climbing enabled. Walk against walls to ascend.");
        }
    }
}
package org.origins.abilities.abilityImplementations;
import org.origins.abilities.*;

import org.bukkit.entity.Player;
import org.origins.OriginsEvolved;
import org.origins.player.PlayerData;

public class TransformAbility implements Ability {
    @Override
    public String getId() {
        return "transform";
    }

    @Override
    public void onActivate(Player player) {
        PlayerData data = OriginsEvolved.get().getPlayerDataManager().getData(player.getUniqueId());
        Boolean wolf = (Boolean) data.getState("werewolf_form");
        if (wolf != null && wolf) {
            data.setState("werewolf_form", false);
            AbilityUtil.sendActionbar(player, "§eYou return to human form.");
            player.sendMessage("§eYou feel your fur recede.");
        } else {
            data.setState("werewolf_form", true);
            AbilityUtil.sendActionbar(player, "§eYou transform into a wolf!");
            player.sendMessage("§eYou feel your body shift.");
        }
    }
}

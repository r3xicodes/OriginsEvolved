package org.origins.abilities.abilityImplementations;
import org.origins.abilities.*;

import org.bukkit.entity.Player;

public class EnderChestAbility implements Ability {
    @Override
    public String getId() {
        return "ender_chest";
    }

    @Override
    public void onActivate(Player player) {
        AbilityUtil.sendActionbar(player, "§5Ender Chest Access");
        AbilityUtil.showTitle(player, "§5Ender Chest", "", 5, 20, 5);
        player.sendMessage("§5Opening your Ender Chest.");
        player.openInventory(player.getEnderChest());
    }
}

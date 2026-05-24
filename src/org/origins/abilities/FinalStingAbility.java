package org.origins.abilities;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.origins.OriginsEvolved;
import org.origins.player.PlayerData;
import org.origins.abilities.AbilityUtil;

public class FinalStingAbility implements Ability {
    @Override
    public String getId() {
        return "final_sting";
    }

    @Override
    public void onActivate(Player player) {
        PlayerData data = OriginsEvolved.get().getPlayerDataManager().getData(player.getUniqueId());
        Boolean toggled = (Boolean) data.getState("final_sting_active");
        if (toggled != null && toggled) {
            data.setState("final_sting_active", false);
            AbilityUtil.sendActionbar(player, "§cFinal Sting deactivated.");
            player.sendMessage("§cFinal Sting deactivated.");
        } else {
            data.setState("final_sting_active", true);
            AbilityUtil.sendActionbar(player, "§cFinal Sting activated!");
            player.sendMessage("§cFinal Sting activated. Beware your next hit!");
        }
    }
}
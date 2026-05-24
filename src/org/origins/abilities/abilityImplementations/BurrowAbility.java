package org.origins.abilities.abilityImplementations;
import org.origins.abilities.*;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.origins.OriginsEvolved;
import org.origins.player.PlayerData;

public class BurrowAbility implements Ability {
    @Override
    public String getId() {
        return "burrow";
    }

    @Override
    public void onActivate(Player player) {
        PlayerData data = OriginsEvolved.get().getPlayerDataManager().getData(player.getUniqueId());
        Boolean burrowed = (Boolean) data.getState("burrowed");
        if (burrowed != null && burrowed) {
            data.setState("burrowed", false);
            AbilityUtil.sendActionbar(player, "§cYou emerge from the ground.");
            player.sendMessage("§cBurrow ended.");
            player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
        } else {
            data.setState("burrowed", true);
            AbilityUtil.sendActionbar(player, "§aYou burrow into the earth.");
            player.sendMessage("§aBurrowed: taking reduced damage.");
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 600, 0));
        }
    }
}

package org.origins.abilities;

import org.bukkit.entity.Player;
import org.origins.OriginsEvolved;
import org.origins.player.PlayerData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BatFormAbility implements Ability {
    @Override
    public String getId() {
        return "bat_form";
    }

    @Override
    public void onActivate(Player player) {
        PlayerData data = OriginsEvolved.get().getPlayerDataManager().getData(player.getUniqueId());
        Boolean bat = (Boolean) data.getState("bat_form");
        if (bat != null && bat) {
            data.setState("bat_form", false);
            AbilityUtil.sendActionbar(player, "§eReturning from bat form.");
            player.sendMessage("§eYou are no longer a bat.");
            player.removePotionEffect(PotionEffectType.LEVITATION);
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        } else {
            data.setState("bat_form", true);
            AbilityUtil.sendActionbar(player, "§eYou become a bat!");
            player.sendMessage("§eYou sprout wings and fly.");
            player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 400, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 400, 0));
        }
    }
}
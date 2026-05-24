package org.origins.abilities.abilityImplementations;
import org.origins.abilities.*;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class RallyingCryAbility implements Ability {
    @Override
    public String getId() {
        return "rallying_cry";
    }

    @Override
    public void onActivate(Player player) {
        AbilityUtil.sendActionbar(player, "§eRallying Cry!");
        AbilityUtil.showTitle(player, "§eRallying Cry", "", 5, 20, 5);
        player.sendMessage("§eAllies gain Resistance.");
        for (Player p : player.getWorld().getPlayers()) {
            if (p.getLocation().getChunk().equals(player.getLocation().getChunk())) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 200, 0));
            }
        }
    }
}

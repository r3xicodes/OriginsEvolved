package org.origins.abilities.abilityImplementations;
import org.origins.abilities.*;

import org.bukkit.entity.Player;

public class PotionPossessionAbility implements Ability {
    @Override
    public String getId() { return "potion_possession"; }

    @Override
    public void onActivate(Player player) {
        player.getNearbyEntities(5,5,5).forEach(ent -> {
            if (ent instanceof Player) {
                Player p = (Player) ent;
                p.getActivePotionEffects().forEach(pe -> {
                    player.addPotionEffect(pe);
                });
            }
        });
        AbilityUtil.sendActionbar(player, "§5Potions possessed!");
    }
}

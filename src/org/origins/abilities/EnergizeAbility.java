package org.origins.abilities;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EnergizeAbility implements Ability {
    @Override
    public String getId() {
        return "energize";
    }

    @Override
    public void onActivate(Player player) {
        AbilityUtil.sendActionbar(player, "§aYou feel energized!");
        // give regen to nearby players
        player.getNearbyEntities(5,5,5).forEach(ent -> {
            if (ent instanceof Player) {
                ((Player) ent).addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 0));
            }
        });
    }
}
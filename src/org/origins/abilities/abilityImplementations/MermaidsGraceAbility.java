package org.origins.abilities.abilityImplementations;
import org.origins.abilities.*;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.origins.OriginsEvolved;

public class MermaidsGraceAbility implements Ability {
    @Override
    public String getId() { return "mermaids_grace"; }

    @Override
    public void onActivate(Player player) {
        Player owner = player;
        Object val = OriginsEvolved.get().getPlayerDataManager().getData(owner.getUniqueId()).getState("mermaids_grace");
        boolean active = val instanceof Boolean && (Boolean) val;
        active = !active;
        OriginsEvolved.get().getPlayerDataManager().getData(owner.getUniqueId()).setState("mermaids_grace", active);
        if (active) {
            AbilityUtil.sendActionbar(owner, "§aMermaid's Grace enabled");
            owner.sendMessage("You feel the grace of the sea.");
            owner.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, Integer.MAX_VALUE, 0, false, false, false));
        } else {
            AbilityUtil.sendActionbar(owner, "§cMermaid's Grace disabled");
            owner.sendMessage("The grace fades.");
            owner.removePotionEffect(PotionEffectType.WATER_BREATHING);
        }
    }
}

package org.origins.abilities;

import org.bukkit.entity.Player;
import org.bukkit.entity.LivingEntity;

public class ChooserOfTheSlainAbility implements Ability {
    @Override
    public String getId() {
        return "chooser_of_the_slain";
    }

    @Override
    public void onActivate(Player player) {
        AbilityUtil.sendActionbar(player, "§dChooser of the Slain");
        AbilityUtil.showTitle(player, "§dChooser of the Slain", "", 5, 20, 5);
        player.sendMessage("§dTargets are marked for death.");
        for (LivingEntity e : player.getWorld().getEntitiesByClass(LivingEntity.class)) {
            if (e.getLocation().distance(player.getLocation()) < 6 && e.getHealth() < e.getMaxHealth() * 0.3) {
                e.setGlowing(true);
                // heal player and give strength when killed will be handled later
            }
        }
    }
}
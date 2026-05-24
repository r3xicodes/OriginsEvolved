package org.origins.abilities;

import org.bukkit.entity.Player;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.Location;

public class DragonBreathAbility implements Ability {
    @Override
    public String getId() {
        return "dragon_breath";
    }

    @Override
    public void onActivate(Player player) {
        AbilityUtil.sendActionbar(player, "§cDragon Breath!");
        AbilityUtil.showTitle(player, "§cDragon Breath", "", 5, 20, 5);
        player.sendMessage("§cFlames erupt from your mouth.");
        Location loc = player.getLocation().add(player.getLocation().getDirection().multiply(1));
        AreaEffectCloud cloud = player.getWorld().spawn(loc, AreaEffectCloud.class);
        cloud.setRadius(2);
        cloud.setDuration(40);
        cloud.addCustomEffect(new PotionEffect(PotionEffectType.HARM, 1, 0), true);
    }
}
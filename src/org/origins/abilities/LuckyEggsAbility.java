package org.origins.abilities;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class LuckyEggsAbility implements Ability {
    @Override
    public String getId() {
        return "lucky_eggs";
    }

    @Override
    public void onActivate(Player player) {
        AbilityUtil.sendActionbar(player, "§6Lucky eggs hatch!");
        // spawn a random mob nearby
        EntityType[] choices = new EntityType[]{EntityType.COW, EntityType.PIG, EntityType.SHEEP, EntityType.CHICKEN, EntityType.ZOMBIE, EntityType.SKELETON};
        EntityType pick = choices[(int) (Math.random() * choices.length)];
        Location loc = player.getLocation().add(player.getLocation().getDirection().multiply(2));
        player.getWorld().spawnEntity(loc, pick);
    }
}
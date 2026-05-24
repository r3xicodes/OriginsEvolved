package org.origins.abilities;

import org.bukkit.entity.Player;

public class TunnelDigAbility implements Ability {
    @Override
    public String getId() {
        return "tunnel_dig";
    }

    @Override
    public void onActivate(Player player) {
        AbilityUtil.sendActionbar(player, "§6Tunnel Dig!");
        AbilityUtil.showTitle(player, "§6Tunnel Dig", "", 5, 20, 5);
        player.sendMessage("§6You can now dig through soft blocks instantly for a short time.");
        // actual digging logic would be handled in block break listener
    }
}
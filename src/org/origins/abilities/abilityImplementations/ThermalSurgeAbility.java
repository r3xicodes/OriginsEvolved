package org.origins.abilities.abilityImplementations;
import org.origins.abilities.*;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.origins.OriginsEvolved;
import org.origins.player.PlayerData;
import org.origins.abilities.AbilityUtil;

public class ThermalSurgeAbility implements Ability {
    @Override
    public String getId() {
        return "thermal_surge";
    }

    @Override
    public void onActivate(Player player) {
        // clear all potion effects
        player.getActivePotionEffects().forEach(pe -> {
            player.removePotionEffect(pe.getType());
        });
        // increase heat modestly
        PlayerData data = OriginsEvolved.get().getPlayerDataManager().getData(player.getUniqueId());
        Integer heat = (Integer) data.getState("heat_bar");
        if (heat == null) heat = 0;
        heat += 500;
        data.setState("heat_bar", heat);
        float progress = Math.min(1f, heat / 10000f);
        org.origins.bossbar.BossBarManager.setBar(player, "Heat: " + heat + "°C", progress, org.bukkit.boss.BarColor.RED);
        AbilityUtil.sendActionbar(player, "§1Thermal surge! Heat now " + heat + "°C");
        player.sendMessage("§1Thermal surge! Heat now " + heat + "°C");
    }
}

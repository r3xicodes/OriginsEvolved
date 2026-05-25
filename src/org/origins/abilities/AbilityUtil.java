package org.origins.abilities;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import java.util.HashMap;
import java.util.Map;

public class AbilityUtil {
    // cooldown display uses action bar rather than boss bar; boss bars reserved for origin-specific effects
    public static void sendAbilityMessage(Player player, String msg) {
        player.sendMessage(ChatColor.AQUA + msg);
    }

    public static void sendActionbar(Player player, String msg) {
        player.sendActionBar(ChatColor.GREEN + msg);
    }

    public static void sendTemporaryActionbar(Player player, String msg, int durationTicks) {
        new BukkitRunnable() {
            final long end = System.currentTimeMillis() + durationTicks * 50L;
            public void run() {
                if (!player.isOnline() || System.currentTimeMillis() >= end) {
                    player.sendActionBar("");
                    cancel();
                } else {
                    player.sendActionBar(msg);
                }
            }
        }.runTaskTimer(org.origins.OriginsEvolved.get(), 0L, 5L);
    }

    public static void showTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
    }

    public static long getCooldownRemaining(Player player, String ability, long cooldown) {
        long now = System.currentTimeMillis();
        long last = org.origins.OriginsEvolved.get().getPlayerDataManager().getData(player.getUniqueId()).getCooldown(ability);
        long end = last + cooldown;
        return Math.max(0, end - now);
    }

    public static boolean tryUse(Player player, String ability, long cooldown) {
        long remaining = getCooldownRemaining(player, ability, cooldown);
        if (remaining > 0) {
            sendTemporaryActionbar(player, ChatColor.RED + "Ability on cooldown: " + (remaining/1000 + 1) + "s", 60);
            return false;
        }
        org.origins.OriginsEvolved.get().getPlayerDataManager().getData(player.getUniqueId()).setCooldown(ability, System.currentTimeMillis());
        return true;
    }
}

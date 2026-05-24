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

    public static void showTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
    }

    public static long getCooldownRemaining(Player player, String ability, long cooldown) {
        long now = System.currentTimeMillis();
        long last = org.origins.OriginsEvolved.get().getPlayerDataManager().getData(player.getUniqueId()).getCooldown(ability);
        long end = last + cooldown;
        return Math.max(0, end - now);
    }

    private static void showCooldownBar(Player player, String ability, long remaining, long total) {
        // show textual countdown on actionbar
        new BukkitRunnable() {
            long end = System.currentTimeMillis() + remaining;
            public void run() {
                long rem = end - System.currentTimeMillis();
                if (rem <= 0) {
                    player.sendActionBar("");
                    cancel();
                } else {
                    player.sendActionBar(ChatColor.RED + ability + " cooldown: " + (rem/1000 + 1) + "s");
                }
            }
        }.runTaskTimer(org.origins.OriginsEvolved.get(), 0L, 20L);
    }

    private static void showBossbarCooldown(Player player, String ability, long remaining, long total) {
        // simple boss bar showing cooldown progress for select abilities
        // colour chosen based on ability name
        org.bukkit.boss.BarColor color = org.bukkit.boss.BarColor.GREEN;
        if (ability.equals("breeze_ball")) color = org.bukkit.boss.BarColor.BLUE;
        if (ability.equals("shriek")) color = org.bukkit.boss.BarColor.PURPLE;
        if (ability.equals("boost")) color = org.bukkit.boss.BarColor.YELLOW;
        // create final copies for inner class
        final String abil = ability;
        final org.bukkit.boss.BarColor col = color;
        // use our manager to create/replace the bar
        org.origins.bossbar.BossBarManager.setBar(player, abil + " CD", (float) remaining / total, col);
        new BukkitRunnable() {
            long end = System.currentTimeMillis() + remaining;
            public void run() {
                long rem = end - System.currentTimeMillis();
                if (rem <= 0) {
                    org.origins.bossbar.BossBarManager.removeBar(player);
                    cancel();
                } else {
                    org.origins.bossbar.BossBarManager.setBar(player, abil + " CD", (float) rem / total, col);
                }
            }
        }.runTaskTimer(org.origins.OriginsEvolved.get(), 0L, 20L);
    }

    // abilities that should use a boss bar instead of action bar for cooldown
    private static final java.util.Set<String> bossBarAbilities = java.util.Set.of("breeze_ball", "shriek", "boost");

    public static boolean tryUse(Player player, String ability, long cooldown) {
        long remaining = getCooldownRemaining(player, ability, cooldown);
        if (remaining > 0) {
            sendActionbar(player, "§cAbility on cooldown: " + (remaining/1000) + "s");
            if (bossBarAbilities.contains(ability)) {
                showBossbarCooldown(player, ability, remaining, cooldown);
            } else {
                showCooldownBar(player, ability, remaining, cooldown);
            }
            return false;
        }
        org.origins.OriginsEvolved.get().getPlayerDataManager().getData(player.getUniqueId()).setCooldown(ability, System.currentTimeMillis());
        // start periodic bar update for the full cooldown
        if (bossBarAbilities.contains(ability)) {
            showBossbarCooldown(player, ability, cooldown, cooldown);
        } else {
            showCooldownBar(player, ability, cooldown, cooldown);
        }
        return true;
    }
}

package org.origins.bossbar;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Simple wrapper for managing a single boss bar per player.
 * Other subsystems can call {@link #setBar(Player, String, float)} to display
 * a progress-style bar, and {@link #removeBar(Player)} to clear it.
 */
public class BossBarManager {
    private static final Map<UUID, BossBar> bars = new HashMap<>();

    public static void setBar(Player player, String title, float progress) {
        setBar(player, title, progress, BarColor.WHITE);
    }

    public static void setBar(Player player, String title, float progress, BarColor color) {
        if (progress < 0f) progress = 0f;
        if (progress > 1f) progress = 1f;
        UUID id = player.getUniqueId();
        BossBar bar = bars.get(id);
        if (bar == null) {
            bar = Bukkit.createBossBar(title, color, BarStyle.SOLID);
            bar.addPlayer(player);
            bars.put(id, bar);
        }
        bar.setTitle(title);
        bar.setProgress(progress);
        bar.setColor(color);
    }

    public static void removeBar(Player player) {
        UUID id = player.getUniqueId();
        BossBar bar = bars.remove(id);
        if (bar != null) {
            bar.removeAll();
        }
    }

    /**
     * Remove all boss bars for everyone (used on shutdown, etc.)
     */
    public static void clearAll() {
        for (BossBar b : bars.values()) {
            b.removeAll();
        }
        bars.clear();
    }
}
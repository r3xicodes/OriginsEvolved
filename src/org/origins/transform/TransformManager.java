package org.origins.transform;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class TransformManager {
    private final JavaPlugin plugin;

    public TransformManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void disguiseAs(Player player, String disguise) {
        // use LibsDisguises API if available
        if (plugin.getServer().getPluginManager().isPluginEnabled("LibsDisguises")) {
            try {
                Class<?> dclass = Class.forName("me.libraryaddict.disguise.DisguiseAPI");
                // reflection to call disguise
            } catch (ClassNotFoundException e) {
                plugin.getLogger().warning("LibsDisguises not found when disguising");
            }
        }
    }
}

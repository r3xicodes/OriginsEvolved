package org.origins.items;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemManager {
    private final JavaPlugin plugin;
    private Map<String, ItemStack> customItems = new HashMap<>();
    private Map<String, String> abilityMap = new HashMap<>(); // item id -> ability id

    public ItemManager(JavaPlugin plugin) {
        this.plugin = plugin;
        loadItems();
    }

    private void loadItems() {
        plugin.saveResource("items.yml", false);
        File file = new File(plugin.getDataFolder(), "items.yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        for (String key : cfg.getKeys(false)) {
            String display = cfg.getString(key + ".display_name");
            List<String> lore = cfg.getStringList(key + ".lore");
            String ability = cfg.getString(key + ".ability");
            String rarity = cfg.getString(key + ".rarity");
            // default material: ender pearl for orbs
            org.bukkit.Material mat = org.bukkit.Material.ENDER_PEARL;
            if (key.startsWith("orb_of_origin")) mat = org.bukkit.Material.DRAGON_EGG;
            ItemStack item = new ItemStack(mat);
            org.bukkit.inventory.meta.ItemMeta meta = item.getItemMeta();
            if (display != null) meta.setDisplayName(org.bukkit.ChatColor.translateAlternateColorCodes('&', display));
            if (!lore.isEmpty()) {
                List<String> coloured = new java.util.ArrayList<>();
                for (String line : lore) coloured.add(org.bukkit.ChatColor.translateAlternateColorCodes('&', line));
                meta.setLore(coloured);
            }
            if (rarity != null) {
                // add rarity to lore
                List<String> existing = meta.hasLore() ? meta.getLore() : new java.util.ArrayList<>();
                existing.add(org.bukkit.ChatColor.GRAY + "Rarity: " + rarity);
                meta.setLore(existing);
            }
            item.setItemMeta(meta);
            customItems.put(key, item);
            if (ability != null) {
                abilityMap.put(key, ability);
            }
        }
    }

    public ItemStack getCustomItem(String id) {
        return customItems.get(id) == null ? null : customItems.get(id).clone();
    }

    public String getItemAbility(String id) {
        return abilityMap.get(id);
    }

    public java.util.Set<String> getItemIds() {
        return customItems.keySet();
    }

}

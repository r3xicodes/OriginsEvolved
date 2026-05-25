package org.origins.attributes;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.origins.OriginsEvolved;
import org.origins.origins.Origin;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.entity.Player;

public class AttributeManager {
    private final OriginsEvolved plugin;
    // originId -> attribute name -> value
    private final Map<String, Map<String, Double>> originAttributes = new HashMap<>();
    // player UUID -> attribute -> modifier UUID
    private final Map<UUID, Map<Attribute, UUID>> activeModifiers = new HashMap<>();
    // player UUID -> active potion types we added
    private final Map<UUID, List<PotionEffectType>> activePotions = new HashMap<>();
    // player UUID -> slot -> previous efficiency level (-1 = none)
    private final Map<UUID, Map<Integer, Integer>> activeToolEnchants = new HashMap<>();

    public AttributeManager(OriginsEvolved plugin) {
        this.plugin = plugin;
        loadFromOriginsInfo();
    }

    private void loadFromOriginsInfo() {
        // Prefer structured YAML file `origins_attributes.yml` from the plugin data folder first,
        // then fall back to the bundled resource. This enables runtime edits in the data folder.
        try {
            java.io.File dataFile = new java.io.File(plugin.getDataFolder(), "origins_attributes.yml");
            InputStream yin = null;
            if (dataFile.exists()) {
                yin = new java.io.FileInputStream(dataFile);
            } else {
                InputStream resourceStream = plugin.getResource("origins_attributes.yml");
                if (resourceStream != null) {
                    yin = resourceStream;
                    try {
                        plugin.saveResource("origins_attributes.yml", false);
                    } catch (Throwable ignored) {
                        // ignore if the file already exists or cannot be copied
                    }
                }
            }
            if (yin != null) {
                try (InputStreamReader reader = new InputStreamReader(yin, StandardCharsets.UTF_8)) {
                    org.bukkit.configuration.file.FileConfiguration cfg = org.bukkit.configuration.file.YamlConfiguration.loadConfiguration(reader);
                    for (String key : cfg.getKeys(false)) {
                        org.bukkit.configuration.ConfigurationSection sec = cfg.getConfigurationSection(key);
                        if (sec == null) continue;
                        Map<String, Double> map = new HashMap<>();
                        for (String sub : sec.getKeys(false)) {
                            try { map.put(sub, sec.getDouble(sub)); } catch (Throwable ignored) {}
                        }
                        if (!map.isEmpty()) originAttributes.put(key, map);
                    }
                }
                return;
            }
        } catch (Throwable t) {
            plugin.getLogger().warning("Failed to load origins_attributes.yml: " + t.getMessage());
        }

        // fallback: parse originsinfo.md heuristically
        try {
            java.io.File mdFile = new java.io.File(plugin.getDataFolder(), "originsinfo.md");
            InputStream in = null;
            if (mdFile.exists()) {
                in = new java.io.FileInputStream(mdFile);
            } else {
                in = plugin.getResource("originsinfo.md");
                if (in == null) return;
                try {
                    plugin.saveResource("originsinfo.md", false);
                } catch (Throwable ignored) {
                    // ignore if the file already exists or cannot be copied
                }
            }
            if (in == null) return;
            BufferedReader r = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            List<String> lines = new ArrayList<>();
            String ln;
            while ((ln = r.readLine()) != null) lines.add(ln);
            r.close();

            // for each known origin, attempt to parse attributes from nearby section
            for (Map.Entry<String, Origin> e : plugin.getOriginManager().getAllOrigins().entrySet()) {
                String originId = e.getKey();
                Origin o = e.getValue();
                String display = o.getDisplayName();
                int idx = -1;
                for (int i = 0; i < lines.size(); i++) {
                    if (lines.get(i).trim().equalsIgnoreCase(display)) { idx = i; break; }
                }
                if (idx == -1) continue;
                // scan next 40 lines for attributes
                int end = Math.min(lines.size(), idx + 40);
                Map<String, Double> map = new HashMap<>();
                for (int i = idx; i < end; i++) {
                    String s = lines.get(i).trim();
                    // Speed: 105% (Sneaking: 30%)
                    java.util.regex.Matcher m = java.util.regex.Pattern.compile("Speed:\\s*([0-9.]+)%").matcher(s);
                    if (m.find()) map.put("movement_speed_pct", Double.parseDouble(m.group(1)));
                    m = java.util.regex.Pattern.compile("Mining Speed:\\s*([0-9.]+)%").matcher(s);
                    if (m.find()) map.put("mining_speed_pct", Double.parseDouble(m.group(1)));
                    m = java.util.regex.Pattern.compile("Mining Fatigue:\\s*([0-9.]+)%").matcher(s);
                    if (m.find()) map.put("mining_fatigue_pct", Double.parseDouble(m.group(1)));
                    m = java.util.regex.Pattern.compile("Size:\\s*([0-9.]+)%").matcher(s);
                    if (m.find()) map.put("size_pct", Double.parseDouble(m.group(1)));
                    m = java.util.regex.Pattern.compile("Fall Damage Taken:\\s*([0-9.]+)%").matcher(s);
                    if (m.find()) map.put("fall_damage_pct", Double.parseDouble(m.group(1)));
                    m = java.util.regex.Pattern.compile("Health:\\s*([0-9.]+)").matcher(s);
                    if (m.find()) map.put("max_health", Double.parseDouble(m.group(1)));
                    m = java.util.regex.Pattern.compile("Base:\\s*([0-9.]+)").matcher(s);
                    if (m.find()) map.putIfAbsent("attack_base", Double.parseDouble(m.group(1)));
                    m = java.util.regex.Pattern.compile("Base Speed:\\s*([0-9.]+)%").matcher(s);
                    if (m.find()) map.putIfAbsent("mining_speed_pct", Double.parseDouble(m.group(1)));
                }
                if (!map.isEmpty()) originAttributes.put(originId, map);
            }
        } catch (Throwable t) {
            plugin.getLogger().warning("Failed to parse originsinfo.md for attributes: " + t.getMessage());
        }
    }

    public void resetAttributes(Player player) {
        UUID id = player.getUniqueId();
        Map<Attribute, UUID> mods = activeModifiers.remove(id);
        if (mods != null) {
            mods.forEach((attr, mu) -> {
                try {
                    org.bukkit.attribute.AttributeInstance inst = player.getAttribute(attr);
                    if (inst != null) {
                        AttributeModifier existing = inst.getModifier(mu);
                        if (existing != null) inst.removeModifier(existing);
                    }
                } catch (Throwable ignored) {}
            });
        }
        // restore vanilla defaults where applicable
        try {
            org.bukkit.attribute.AttributeInstance hp = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if (hp != null) {
                // ensure no modifiers remain that would change max health
            }
            // reset health to not exceed max
            double max = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
            if (player.getHealth() > max) player.setHealth(max);
        } catch (Throwable ignored) {}
        // remove potion effects we applied
        try {
            List<PotionEffectType> types = activePotions.remove(id);
            if (types != null) {
                for (PotionEffectType pt : types) {
                    try { player.removePotionEffect(pt); } catch (Throwable ignored) {}
                }
            }
        } catch (Throwable ignored) {}
        // restore tool enchantments
        try {
            Map<Integer, Integer> prev = activeToolEnchants.remove(id);
            if (prev != null) {
                org.bukkit.inventory.PlayerInventory inv = player.getInventory();
                for (Map.Entry<Integer, Integer> e : prev.entrySet()) {
                    int slot = e.getKey();
                    int lvl = e.getValue();
                    try {
                        ItemStack it = inv.getItem(slot);
                        if (it == null) continue;
                        if (lvl == -1) it.removeEnchantment(Enchantment.DIG_SPEED);
                        else it.addUnsafeEnchantment(Enchantment.DIG_SPEED, lvl);
                        inv.setItem(slot, it);
                    } catch (Throwable ignored) {}
                }
            }
        } catch (Throwable ignored) {}
        // try to reset size if supported
        try {
            java.lang.reflect.Method m = player.getClass().getMethod("setScale", float.class);
            if (m != null) {
                m.invoke(player, 1.0f);
            }
        } catch (NoSuchMethodException ignored) {
        } catch (Throwable t) { plugin.getLogger().fine("Failed to reset player scale: " + t.getMessage()); }
    }

    public void applyAttributes(Player player, String originId) {
        resetAttributes(player);
        Map<String, Double> map = originAttributes.get(originId);
        if (map == null || map.isEmpty()) return;
        UUID pid = player.getUniqueId();
        Map<Attribute, UUID> mods = new HashMap<>();
        List<PotionEffectType> addedPotions = new ArrayList<>();

        // movement speed
        if (map.containsKey("movement_speed_pct")) {
            double pct = map.get("movement_speed_pct") / 100.0;
            // multiplier relative to base: value should be pct. Use MULTIPLY_SCALAR_1 with (pct - 1)
            double amount = pct - 1.0;
            Attribute attr = Attribute.GENERIC_MOVEMENT_SPEED;
            UUID mu = UUID.nameUUIDFromBytes((pid.toString() + ":movement_speed").getBytes(StandardCharsets.UTF_8));
            AttributeModifier mod = new AttributeModifier(mu, "origin_movement_speed", amount, AttributeModifier.Operation.MULTIPLY_SCALAR_1);
            org.bukkit.attribute.AttributeInstance inst = player.getAttribute(attr);
            if (inst != null) inst.addModifier(mod);
            mods.put(attr, mu);
        }

        // max health
        if (map.containsKey("max_health")) {
            double base = map.get("max_health");
            Attribute attr = Attribute.GENERIC_MAX_HEALTH;
            UUID mu = UUID.nameUUIDFromBytes((pid.toString() + ":max_health").getBytes(StandardCharsets.UTF_8));
            double add = base - player.getAttribute(attr).getBaseValue();
            AttributeModifier mod = new AttributeModifier(mu, "origin_max_health", add, AttributeModifier.Operation.ADD_NUMBER);
            org.bukkit.attribute.AttributeInstance inst = player.getAttribute(attr);
            if (inst != null) inst.addModifier(mod);
            // heal or clamp health
            if (player.getHealth() > inst.getValue()) player.setHealth(inst.getValue());
            mods.put(attr, mu);
        }

        // attack base
        if (map.containsKey("attack_base")) {
            double base = map.get("attack_base");
            Attribute attr = Attribute.GENERIC_ATTACK_DAMAGE;
            UUID mu = UUID.nameUUIDFromBytes((pid.toString() + ":attack_damage").getBytes(StandardCharsets.UTF_8));
            double add = base - player.getAttribute(attr).getBaseValue();
            AttributeModifier mod = new AttributeModifier(mu, "origin_attack_damage", add, AttributeModifier.Operation.ADD_NUMBER);
            org.bukkit.attribute.AttributeInstance inst = player.getAttribute(attr);
            if (inst != null) inst.addModifier(mod);
            mods.put(attr, mu);
        }

        // mining speed / fatigue
        // mining_speed_pct >100 -> give HASTE; <100 -> give SLOW_DIGGING
        if (map.containsKey("mining_speed_pct")) {
            double pct = map.get("mining_speed_pct");
            try {
                double mult = pct / 100.0;
                // determine potion-based haste first for moderate multipliers
                if (mult >= 1.05 && mult < 1.35) {
                    // small boost -> Haste I
                    player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 0, false, false, false), true);
                    addedPotions.add(PotionEffectType.FAST_DIGGING);
                } else if (mult >= 1.35 && mult < 1.8) {
                    // stronger -> Haste II
                    player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 1, false, false, false), true);
                    addedPotions.add(PotionEffectType.FAST_DIGGING);
                } else if (mult >= 1.8) {
                    // huge -> Haste III
                    player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 2, false, false, false), true);
                    addedPotions.add(PotionEffectType.FAST_DIGGING);
                } else if (mult < 0.95) {
                    // slower -> apply Slow Digging
                    int amp = mult < 0.6 ? 1 : 0;
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, Integer.MAX_VALUE, amp, false, false, false), true);
                    addedPotions.add(PotionEffectType.SLOW_DIGGING);
                }

                // to get closer to exact multiplier, compute target efficiency level
                int effLevel = Math.max(0, Math.min(10, (int) Math.round((mult - 1.0) * 4.0)));
                if (effLevel > 0) {
                    applyEfficiencyToInventory(player, pid, effLevel);
                }
            } catch (Throwable ignored) {}
        }

        if (map.containsKey("mining_fatigue_pct")) {
            double pct = map.get("mining_fatigue_pct");
            try {
                if (pct < 100) {
                    // less fatigue -> maybe give haste instead
                } else if (pct >= 100) {
                    // apply slow digging as fatigue
                    int amp = pct > 150 ? 1 : 0;
                    PotionEffect pe = new PotionEffect(PotionEffectType.SLOW_DIGGING, Integer.MAX_VALUE, amp, false, false, false);
                    player.addPotionEffect(pe, true);
                    addedPotions.add(PotionEffectType.SLOW_DIGGING);
                }
            } catch (Throwable ignored) {}
        }

        // size scaling (best-effort via reflection: setScale(float))
        if (map.containsKey("size_pct")) {
            double pct = map.get("size_pct");
            try {
                float scale = (float) (pct / 100.0);
                java.lang.reflect.Method m = player.getClass().getMethod("setScale", float.class);
                if (m != null) m.invoke(player, scale);
            } catch (NoSuchMethodException ignored) {
            } catch (Throwable t) { plugin.getLogger().fine("Failed to set player scale: " + t.getMessage()); }
        }

        if (!addedPotions.isEmpty()) activePotions.put(pid, addedPotions);

        if (!mods.isEmpty()) activeModifiers.put(pid, mods);
    }

    private void applyEfficiencyToInventory(Player player, UUID pid, int effLevel) {
        try {
            Map<Integer, Integer> prev = new HashMap<>();
            org.bukkit.inventory.PlayerInventory inv = player.getInventory();
            for (int i = 0; i < inv.getSize(); i++) {
                ItemStack it = inv.getItem(i);
                if (it == null) continue;
                Material m = it.getType();
                if (m.name().endsWith("_PICKAXE") || m.name().endsWith("_SHOVEL") || m.name().endsWith("_AXE") || m.name().endsWith("_HOE")) {
                    int prevLvl = it.getEnchantmentLevel(Enchantment.DIG_SPEED);
                    prev.put(i, prevLvl > 0 ? prevLvl : -1);
                    try { it.addUnsafeEnchantment(Enchantment.DIG_SPEED, effLevel); } catch (Throwable ignored) {}
                    inv.setItem(i, it);
                }
            }
            if (!prev.isEmpty()) activeToolEnchants.put(pid, prev);
        } catch (Throwable ignored) {}
    }

    public void refreshToolEnchants(Player player) {
        // restore previous
        try {
            UUID pid = player.getUniqueId();
            Map<Integer, Integer> prev = activeToolEnchants.remove(pid);
            if (prev != null) {
                org.bukkit.inventory.PlayerInventory inv = player.getInventory();
                for (Map.Entry<Integer, Integer> e : prev.entrySet()) {
                    int slot = e.getKey();
                    int lvl = e.getValue();
                    try {
                        ItemStack it = inv.getItem(slot);
                        if (it == null) continue;
                        if (lvl == -1) it.removeEnchantment(Enchantment.DIG_SPEED);
                        else it.addUnsafeEnchantment(Enchantment.DIG_SPEED, lvl);
                        inv.setItem(slot, it);
                    } catch (Throwable ignored) {}
                }
            }
            // now reapply based on origin attributes
            org.origins.player.PlayerData d = plugin.getPlayerDataManager().getData(player.getUniqueId());
            String origin = d.getOrigin();
            if (origin == null) return;
            Map<String, Double> map = originAttributes.get(origin);
            if (map == null) return;
            if (map.containsKey("mining_speed_pct")) {
                double pct = map.get("mining_speed_pct");
                double mult = pct / 100.0;
                int effLevel = Math.max(0, Math.min(10, (int) Math.round((mult - 1.0) * 4.0)));
                if (effLevel > 0) applyEfficiencyToInventory(player, player.getUniqueId(), effLevel);
            }
        } catch (Throwable ignored) {}
    }

    public Map<String, Map<String, Double>> getOriginAttributes() { return originAttributes; }
}

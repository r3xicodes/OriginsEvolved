package org.origins;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.inventory.ItemStack;
import org.origins.commands.OriginCommand;
import org.origins.player.PlayerData;
import org.origins.player.PlayerDataManager;
import org.origins.origins.OriginManager;
import org.origins.evolution.EvolutionManager;
import org.origins.items.ItemManager;
import org.origins.transform.TransformManager;
import org.origins.listeners.PlayerRespawnListener;

public class OriginsEvolved extends JavaPlugin {
    private static OriginsEvolved instance;

    private PlayerDataManager playerDataManager;
    private OriginManager originManager;
    private EvolutionManager evolutionManager;
    private ItemManager itemManager;
    private TransformManager transformManager;
    private org.origins.abilities.AbilityManager abilityManager;
    private org.origins.attributes.AttributeManager attributeManager;

    public static OriginsEvolved get() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        saveResource("origins_attributes.yml", false);
        saveResource("originsinfo.md", false);
        reloadConfigs();
        setupManagers();
        getCommand("origin").setExecutor(new OriginCommand());
        // register event listeners here
        getServer().getPluginManager().registerEvents(new org.origins.listeners.PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new org.origins.listeners.InventoryClickListener(), this);
        getServer().getPluginManager().registerEvents(new org.origins.listeners.BossKillListener(), this);
        getServer().getPluginManager().registerEvents(new org.origins.listeners.PlayerInteractListener(), this);
        getServer().getPluginManager().registerEvents(new org.origins.listeners.MovementListener(), this);
        getServer().getPluginManager().registerEvents(new org.origins.listeners.PotionEffectListener(), this);
        // boss bar cleanup
        getServer().getPluginManager().registerEvents(new org.origins.listeners.BossBarListener(), this);
        getServer().getPluginManager().registerEvents(new org.origins.listeners.KeybindListener(), this);
        // attribute listener for fall-damage and other attribute-driven behavior
        getServer().getPluginManager().registerEvents(new org.origins.attributes.AttributeListener(this, attributeManager), this);
        // inventory listener to keep tool enchants in sync when players move items or switch slots
        getServer().getPluginManager().registerEvents(new org.origins.attributes.InventoryListener(this, attributeManager), this);
        // listen for respawn to reset origin bars
        getServer().getPluginManager().registerEvents(new org.origins.listeners.PlayerRespawnListener(), this);
        // playtime tracking every minute
        getServer().getScheduler().runTaskTimer(this, () -> {
            getServer().getOnlinePlayers().forEach(p -> {
                PlayerData data = playerDataManager.getData(p.getUniqueId());
                if (data.getOrigin() != null) {
                    data.addTime(60);
                }
            });
        }, 1200L, 1200L);

        // periodic status update: hydration, sun exposure, elytrian wing strain, etc.
        getServer().getScheduler().runTaskTimer(this, () -> {
            getServer().getOnlinePlayers().forEach(p -> {
                PlayerData data = playerDataManager.getData(p.getUniqueId());
                String orig = data.getOrigin();
                if (orig == null) return;
                // MERLING hydration
                if ("merling".equals(orig)) {
                    boolean inWater = p.getLocation().getBlock().isLiquid();
                    int hydration = data.getIntState("hydration");
                    if (inWater) hydration = Math.min(100, hydration + 1);
                    else hydration = Math.max(0, hydration - 1);
                    data.setIntState("hydration", hydration);
                    float prog = hydration / 100f;
                    org.origins.bossbar.BossBarManager.setBar(p, "Hydration: " + hydration + "%", prog, org.bukkit.boss.BarColor.BLUE);
                    if (hydration == 0) {
                        int dry = data.getIntState("dry_timer") + 1;
                        data.setIntState("dry_timer", dry);
                        p.damage(1.0);
                        if (dry >= 10) {
                            p.setHealth(0);
                        }
                    } else {
                        data.setIntState("dry_timer", 0);
                    }
                }
                // PHANTOM sun exposure
                if ("phantom".equals(orig)) {
                    boolean daytime = p.getWorld().getTime() % 24000 < 12000;
                    boolean sunny = daytime && p.getLocation().getBlock().getLightFromSky() > 10;
                    int exposure = data.getIntState("sun_exposure");
                    if (sunny) exposure = Math.min(100, exposure + 5);
                    else exposure = Math.max(0, exposure - 5);
                    data.setIntState("sun_exposure", exposure);
                    float prog = exposure / 100f;
                    org.origins.bossbar.BossBarManager.setBar(p, "Sun Exposure: " + exposure + "%", prog, org.bukkit.boss.BarColor.YELLOW);
                    if (exposure == 100) {
                        int timer = data.getIntState("sun_damage_timer") + 1;
                        data.setIntState("sun_damage_timer", timer);
                        p.damage(1.0);
                        if (timer >= 5) {
                            p.setHealth(0);
                        }
                    } else {
                        data.setIntState("sun_damage_timer", 0);
                    }
                }
                // ELYTRIAN wing charge while gliding
                if ("elytrian".equals(orig)) {
                    if (p.isGliding()) {
                        int charge = data.getIntState("wing_charge") + 1;
                        data.setIntState("wing_charge", Math.min(100, charge));
                        float prog = charge / 100f;
                        org.origins.bossbar.BossBarManager.setBar(p, "Wing Strain: " + charge + "%", prog, org.bukkit.boss.BarColor.PURPLE);
                        if (charge >= 100) {
                            p.setGliding(false);
                            if (p.getInventory().getChestplate() != null && p.getInventory().getChestplate().getType() == org.bukkit.Material.ELYTRA) {
                                p.getInventory().setChestplate(null);
                                p.sendMessage(org.bukkit.ChatColor.RED + "Your wings have fallen off from strain!");
                            }
                        }
                    } else {
                        data.setIntState("wing_charge", 0);
                    }
                }
            });
        }, 20L, 20L);

        registerRecipes();
        getLogger().info("OriginsEvolved enabled!");
    }

    private void registerRecipes() {
        // easy-to-craft generic ability orb
        ItemStack orb = getItemManager().getCustomItem("ability_orb");
        if (orb != null) {
            org.bukkit.NamespacedKey key = new org.bukkit.NamespacedKey(this, "ability_orb");
            org.bukkit.inventory.ShapedRecipe recipe = new org.bukkit.inventory.ShapedRecipe(key, orb);
            recipe.shape(" E ", "EPE", " E ");
            recipe.setIngredient('E', org.bukkit.Material.ENDER_PEARL);
            recipe.setIngredient('P', org.bukkit.Material.GLOWSTONE_DUST);
            getServer().addRecipe(recipe);
        }
        // Orb of Origin: expensive
        ItemStack originOrb = getItemManager().getCustomItem("orb_of_origin");
        if (originOrb != null) {
            org.bukkit.NamespacedKey key2 = new org.bukkit.NamespacedKey(this, "orb_of_origin");
            org.bukkit.inventory.ShapedRecipe r2 = new org.bukkit.inventory.ShapedRecipe(key2, originOrb);
            r2.shape("DND", "NGN", "DND");
            r2.setIngredient('D', org.bukkit.Material.DRAGON_EGG);
            r2.setIngredient('N', org.bukkit.Material.NETHER_STAR);
            r2.setIngredient('G', org.bukkit.Material.EMERALD_BLOCK);
            getServer().addRecipe(r2);
        }
        // caster recipes
        ItemStack primCaster = getItemManager().getCustomItem("ability_caster_primary");
        if (primCaster != null) {
            org.bukkit.NamespacedKey key3 = new org.bukkit.NamespacedKey(this, "ability_caster_primary");
            org.bukkit.inventory.ShapedRecipe r3 = new org.bukkit.inventory.ShapedRecipe(key3, primCaster);
            r3.shape("  B", " B ", "B  ");
            r3.setIngredient('B', org.bukkit.Material.BLAZE_ROD);
            getServer().addRecipe(r3);
        }
        ItemStack secCaster = getItemManager().getCustomItem("ability_caster_secondary");
        if (secCaster != null) {
            org.bukkit.NamespacedKey key4 = new org.bukkit.NamespacedKey(this, "ability_caster_secondary");
            org.bukkit.inventory.ShapedRecipe r4 = new org.bukkit.inventory.ShapedRecipe(key4, secCaster);
            r4.shape("  S", " S ", "S  ");
            r4.setIngredient('S', org.bukkit.Material.STICK);
            getServer().addRecipe(r4);
        }
    }

    @Override
    public void onDisable() {
        if (playerDataManager != null) {
            playerDataManager.saveAll();
        }
        getLogger().info("OriginsEvolved disabled!");
    }

    private void setupManagers() {
        playerDataManager = new PlayerDataManager(this);
        originManager = new OriginManager(this);
        evolutionManager = new EvolutionManager(this);
        itemManager = new ItemManager(this);
        transformManager = new TransformManager(this);
        abilityManager = new org.origins.abilities.AbilityManager(this);
        attributeManager = new org.origins.attributes.AttributeManager(this);
    }

    public void reloadConfigs() {
        reloadConfig();
        originManager = new OriginManager(this);
        evolutionManager = new EvolutionManager(this);
        itemManager = new ItemManager(this);
        abilityManager = new org.origins.abilities.AbilityManager(this);
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }

    public OriginManager getOriginManager() {
        return originManager;
    }

    public EvolutionManager getEvolutionManager() {
        return evolutionManager;
    }

    public ItemManager getItemManager() {
        return itemManager;
    }

    public TransformManager getTransformManager() {
        return transformManager;
    }

    public org.origins.abilities.AbilityManager getAbilityManager() {
        return abilityManager;
    }

    public org.origins.attributes.AttributeManager getAttributeManager() { return attributeManager; }

    /**
     * Set a player's origin with proper attribute reset and application.
     */
    public void setPlayerOrigin(org.bukkit.entity.Player player, String originId) {
        // reset attributes to vanilla defaults first
        if (attributeManager != null) attributeManager.resetAttributes(player);
        // update stored origin
        org.origins.player.PlayerData data = playerDataManager.getData(player.getUniqueId());
        data.setOrigin(originId);
        data.setTimePlayedWithOrigin(0);
        // apply new origin attributes
        if (attributeManager != null) attributeManager.applyAttributes(player, originId);
    }

    public void resetPlayerOrigin(org.bukkit.entity.Player player) {
        if (attributeManager != null) attributeManager.resetAttributes(player);
        org.origins.player.PlayerData data = playerDataManager.getData(player.getUniqueId());
        data.setOrigin(null);
        data.setTimePlayedWithOrigin(0);
    }

    // ---------------- config helpers ----------------
    public int getAbilityCooldown(String abilityId) {
        String path = "ability_cooldowns." + abilityId;
        if (getConfig().contains(path)) {
            return getConfig().getInt(path);
        }
        return getConfig().getInt("ability_cooldowns.default", 5);
    }

    public int getEvolutionRequirement(String originId) {
        String path = "evolution_requirements." + originId;
        if (getConfig().contains(path)) {
            return getConfig().getInt(path);
        }
        return getConfig().getInt("evolution_requirements.default", 0);
    }

    public int getTransformCooldown() {
        return getConfig().getInt("transform_cooldown", 60);
    }
}

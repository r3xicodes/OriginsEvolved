package org.origins.abilities;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.origins.OriginsEvolved;
import org.origins.abilities.Ability;
import org.origins.abilities.AbilityUtil;
import org.origins.abilities.*;

public class AbilityManager {
    private final JavaPlugin plugin;
    private final Map<String, Ability> abilities = new HashMap<>();

    public AbilityManager(JavaPlugin plugin) {
        this.plugin = plugin;
        // auto-discover abilities in both the root abilities package and
        // the future abilityImplementations subpackage so files can be moved
        loadAbilitiesFromPackage("org.origins.abilities");
        loadAbilitiesFromPackage("org.origins.abilities.abilityImplementations");
    }

    private void loadAbilitiesFromPackage(String packageName) {
        try {
            String path = packageName.replace('.', '/');
            ClassLoader cl = plugin.getClass().getClassLoader();
            Enumeration<URL> resources = cl.getResources(path);
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                if (resource.getProtocol().equals("file")) {
                    File dir = new File(URLDecoder.decode(resource.getPath(), "UTF-8"));
                    File[] files = dir.listFiles();
                    if (files == null) continue;
                    for (File f : files) {
                        if (!f.getName().endsWith(".class")) continue;
                        // derive full class name
                        String className = packageName + "." + f.getName().replace(".class", "");
                        tryLoadAndRegister(className);
                    }
                } else if (resource.getProtocol().equals("jar")) {
                    JarURLConnection conn = (JarURLConnection) resource.openConnection();
                    try (JarFile jar = conn.getJarFile()) {
                        Enumeration<JarEntry> entries = jar.entries();
                        while (entries.hasMoreElements()) {
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            if (!name.startsWith(path) || !name.endsWith(".class")) continue;
                            if (name.contains("$")) continue;
                            String className = name.replace('/', '.').replace(".class", "");
                            tryLoadAndRegister(className);
                        }
                    }
                }
            }
            // fallback file-scan directly from classloader resource
            URL pkg = plugin.getClass().getClassLoader().getResource(path);
            if (pkg != null && pkg.getProtocol().equals("file")) {
                File dir = new File(URLDecoder.decode(pkg.getPath(), "UTF-8"));
                File[] files = dir.listFiles((d, name) -> name.endsWith(".class") && !name.contains("$"));
                if (files != null) {
                    for (File f : files) {
                        String className = packageName + "." + f.getName().replace(".class", "");
                        tryLoadAndRegister(className);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void tryLoadAndRegister(String className) {
        try {
            Class<?> cls = Class.forName(className);
            if (Ability.class.isAssignableFrom(cls) && !Modifier.isAbstract(cls.getModifiers())) {
                Ability ability = (Ability) cls.getDeclaredConstructor().newInstance();
                registerAbility(ability);
            }
        } catch (Throwable t) {
            // ignore classes we can't load/instantiate
        }
    }

    public Ability getAbility(String id) {
        return abilities.get(id);
    }

    public void registerAbility(Ability ability) {
        abilities.put(ability.getId(), ability);
    }

    /**
     * Attempt to activate an ability for a player. Handles cooldown and feedback.
     */
    /**
     * Activate an ability for a player. Cooldown looked up from config.
     */
    public void activate(Player player, String abilityId) {
        if (abilityId == null || abilityId.equals("none")) return;
        int seconds = ((OriginsEvolved) plugin).getAbilityCooldown(abilityId);
        long cooldownMillis = seconds * 1000L;
        if (!AbilityUtil.tryUse(player, abilityId, cooldownMillis)) {
            return; // still cooling
        }
        Ability ability = getAbility(abilityId);
        if (ability == null) {
            player.sendMessage(ChatColor.RED + "Unknown ability: " + abilityId);
            return;
        }
        // show effects and messages with color variation
        String name = ability.getName();
        ChatColor color = ChatColor.AQUA;
        org.bukkit.Particle particle = org.bukkit.Particle.CRIT;
        org.bukkit.Sound sound = org.bukkit.Sound.ENTITY_EXPERIENCE_ORB_PICKUP;
        switch (abilityId) {
            case "glide": color = ChatColor.LIGHT_PURPLE; particle = org.bukkit.Particle.CLOUD; sound = org.bukkit.Sound.ENTITY_PLAYER_SPLASH;
                break;
            case "arcane_analysis": color = ChatColor.DARK_PURPLE; particle = org.bukkit.Particle.ENCHANTMENT_TABLE; sound = org.bukkit.Sound.ENTITY_ENDER_EYE_LAUNCH;
                break;
            case "charge_jump": color = ChatColor.YELLOW; particle = org.bukkit.Particle.FLAME; sound = org.bukkit.Sound.ENTITY_RABBIT_JUMP;
                break;
            case "veilwalk": color = ChatColor.GRAY; particle = org.bukkit.Particle.SMOKE_NORMAL; sound = org.bukkit.Sound.ENTITY_PHANTOM_FLAP;
                break;
            case "pounce": color = ChatColor.GOLD; particle = org.bukkit.Particle.CRIT; sound = org.bukkit.Sound.ENTITY_PLAYER_ATTACK_KNOCKBACK;
                break;
            case "howl": color = ChatColor.DARK_RED; particle = org.bukkit.Particle.WAX_ON; sound = org.bukkit.Sound.ENTITY_WOLF_HOWL;
                break;
            case "roar": color = ChatColor.RED; particle = org.bukkit.Particle.EXPLOSION_LARGE; sound = org.bukkit.Sound.ENTITY_ENDER_DRAGON_GROWL;
                break;
            case "breeze_ball": color = ChatColor.AQUA; particle = org.bukkit.Particle.WATER_SPLASH; sound = org.bukkit.Sound.ENTITY_ARROW_SHOOT;
                break;
            case "dragon_breath": color = ChatColor.DARK_RED; particle = org.bukkit.Particle.DRIP_LAVA; sound = org.bukkit.Sound.ENTITY_BLAZE_SHOOT;
                break;
            // add additional cases as necessary
            default:
                break;
        }
        AbilityUtil.sendActionbar(player, color + "Using " + name);
        AbilityUtil.showTitle(player, color + name, "", 5, 20, 5);
        player.sendMessage(ChatColor.GREEN + "Ability " + name + " activated!");
        // make the player swing their hand as a visual cue
        player.swingMainHand();
        player.getWorld().spawnParticle(particle, player.getLocation().add(0,1,0), 20, 0.2, 0.2, 0.2, 0.05);
        player.playSound(player.getLocation(), sound, 1f, 1f);
        ability.onActivate(player);
    }
}

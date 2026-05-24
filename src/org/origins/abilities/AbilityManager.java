package org.origins.abilities;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.origins.abilities.AbilityUtil;
import org.origins.OriginsEvolved;
import org.origins.abilities.GlideAbility;
import org.origins.abilities.ArcaneAnalysisAbility;
import org.origins.abilities.CatalystAbility;
import org.origins.abilities.ArcaneShieldAbility;
import org.origins.abilities.PollinateAbility;
import org.origins.abilities.FinalStingAbility;
import org.origins.abilities.BuzzAwayAbility;
import org.origins.abilities.ToggleClimbAbility;
import org.origins.abilities.FuelConsumptionAbility;
import org.origins.abilities.ThermalSurgeAbility;
import org.origins.abilities.FireResistanceAbility;
import org.origins.abilities.LavaHealAbility;
// new origins
import org.origins.abilities.LuckyEggsAbility;
import org.origins.abilities.FlutterAbility;
import org.origins.abilities.BoostAbility;
import org.origins.abilities.FoldWingsAbility;
import org.origins.abilities.BlinkAbility;
import org.origins.abilities.DistortionDodgeAbility;
import org.origins.abilities.EnergizeAbility;
import org.origins.abilities.RoarAbility;
import org.origins.abilities.EnlargeAbility;
import org.origins.abilities.PrecisionAbility;
import org.origins.abilities.ChangeTipAbility;
import org.origins.abilities.StalkAbility;
import org.origins.abilities.FleeAbility;
import org.origins.abilities.SwiftSwimAbility;
import org.origins.abilities.MermaidsGraceAbility;
import org.origins.abilities.VeilwalkAbility;
import org.origins.abilities.PotionPossessionAbility;
import org.origins.abilities.ChargeJumpAbility;
import org.origins.abilities.MegaJumpAbility;
// additional new classes
import org.origins.abilities.PounceAbility;
import org.origins.abilities.DashAbility;
import org.origins.abilities.VerdantGraspAbility;
import org.origins.abilities.BarkskinAbility;
import org.origins.abilities.EnderChestAbility;
import org.origins.abilities.LevitationAbility;
import org.origins.abilities.HowlAbility;
import org.origins.abilities.TransformAbility;
import org.origins.abilities.BreezeBallAbility;
import org.origins.abilities.WindLaunchAbility;
import org.origins.abilities.InfectiousBiteAbility;
import org.origins.abilities.TenaciousAbility;
import org.origins.abilities.ShriekAbility;
import org.origins.abilities.ProspectingAbility;
import org.origins.abilities.RallyingCryAbility;
import org.origins.abilities.WingedAscensionAbility;
import org.origins.abilities.ChooserOfTheSlainAbility;
import org.origins.abilities.BloodSurgeAbility;
import org.origins.abilities.BatFormAbility;
import org.origins.abilities.DragonBreathAbility;
import org.origins.abilities.DraconicLeapAbility;
import org.origins.abilities.HornChargeAbility;
import org.origins.abilities.BurrowAbility;
import org.origins.abilities.HiveCallAbility;
import org.origins.abilities.TunnelDigAbility;
import org.origins.abilities.LeapAbility;

public class AbilityManager {
    private final JavaPlugin plugin;
    private final Map<String, Ability> abilities = new HashMap<>();

    public AbilityManager(JavaPlugin plugin) {
        this.plugin = plugin;
        // register built-in abilities here
        registerAbility(new GlideAbility());
        registerAbility(new ArcaneAnalysisAbility());
        registerAbility(new CatalystAbility());
        registerAbility(new ArcaneShieldAbility());
        registerAbility(new PollinateAbility());
        registerAbility(new FinalStingAbility());
        registerAbility(new BuzzAwayAbility());
        registerAbility(new ToggleClimbAbility());
        registerAbility(new FuelConsumptionAbility());
        registerAbility(new ThermalSurgeAbility());
        registerAbility(new FireResistanceAbility());
        registerAbility(new LavaHealAbility());
        // originsinfo abilities
        registerAbility(new LuckyEggsAbility());
        registerAbility(new FlutterAbility());
        registerAbility(new BoostAbility());
        registerAbility(new FoldWingsAbility());
        registerAbility(new BlinkAbility());
        registerAbility(new DistortionDodgeAbility());
        registerAbility(new EnergizeAbility());
        registerAbility(new RoarAbility());
        registerAbility(new EnlargeAbility());
        registerAbility(new PrecisionAbility());
        registerAbility(new ChangeTipAbility());
        registerAbility(new StalkAbility());
        registerAbility(new FleeAbility());
        // merling
        registerAbility(new SwiftSwimAbility());
        registerAbility(new MermaidsGraceAbility());
        // phantom
        registerAbility(new VeilwalkAbility());
        registerAbility(new PotionPossessionAbility());
        // bunny
        registerAbility(new ChargeJumpAbility());
        registerAbility(new MegaJumpAbility());
        // Pawsworn
        registerAbility(new PounceAbility());
        registerAbility(new DashAbility());
        // Phytokin
        registerAbility(new VerdantGraspAbility());
        registerAbility(new BarkskinAbility());
        // Shulk
        registerAbility(new EnderChestAbility());
        registerAbility(new LevitationAbility());
        // Werewolf
        registerAbility(new HowlAbility());
        registerAbility(new TransformAbility());
        registerAbility(new LeapAbility());
        // Breezeborn
        registerAbility(new BreezeBallAbility());
        registerAbility(new WindLaunchAbility());
        // Undead
        registerAbility(new InfectiousBiteAbility());
        registerAbility(new TenaciousAbility());
        // Sculkborn
        registerAbility(new ShriekAbility());
        // Dwarf
        registerAbility(new ProspectingAbility());
        registerAbility(new RallyingCryAbility());
        // Valkyrie
        registerAbility(new WingedAscensionAbility());
        registerAbility(new ChooserOfTheSlainAbility());
        // Vampire
        registerAbility(new BloodSurgeAbility());
        registerAbility(new BatFormAbility());
        // Dragonborne
        registerAbility(new DragonBreathAbility());
        registerAbility(new DraconicLeapAbility());
        // Beetle
        registerAbility(new HornChargeAbility());
        registerAbility(new BurrowAbility());
        // Ant
        registerAbility(new HiveCallAbility());
        registerAbility(new TunnelDigAbility());
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

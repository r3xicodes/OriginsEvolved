package org.origins.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.origins.OriginsEvolved;
import org.origins.origins.Origin;
import org.origins.player.PlayerData;
import org.origins.player.PlayerDataManager;
import org.origins.abilities.Ability;

import java.util.UUID;

public class OriginCommand implements CommandExecutor {
    private final OriginsEvolved plugin = OriginsEvolved.get();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {
                handleChoose((Player) sender);
            } else {
                sender.sendMessage(ChatColor.YELLOW + "Use /origin <choose|menu|evolve|transform|ability|info|set|reset|giveorb>");
            }
            return true;
        }
        String sub = args[0].toLowerCase();
        if (sub.equals("choose")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only players can choose origins.");
                return true;
            }
            Player p = (Player) sender;
            if (!sender.hasPermission("origin.choose")) {
                sender.sendMessage(ChatColor.RED + "You don't have permission.");
                return true;
            }
            handleChoose(p);
            return true;
        } else if (sub.equals("info")) {
            if (!(sender instanceof Player)) return true;
            Player p = (Player) sender;
            PlayerData data = plugin.getPlayerDataManager().getData(p.getUniqueId());
            String originId = data.getOrigin();
            if (originId == null) {
                p.sendMessage(ChatColor.GRAY + "You have no origin.");
            } else {
                Origin o = plugin.getOriginManager().getOrigin(originId);
                if (o != null) {
                    p.sendMessage(ChatColor.GOLD + "Origin: " + o.getDisplayName());
                    p.sendMessage(ChatColor.GRAY + o.getDescription());
                    String prim = o.getPrimaryAbility();
                    if (prim != null && !prim.equals("none")) {
                        p.sendMessage(ChatColor.GREEN + "Primary Ability: " + plugin.getAbilityManager().getAbility(prim).getName());
                    }
                    String sec = o.getSecondaryAbility();
                    if (sec != null && !sec.equals("none")) {
                        p.sendMessage(ChatColor.GREEN + "Secondary Ability: " + plugin.getAbilityManager().getAbility(sec).getName());
                    }
                    String crouch = o.getCrouchAbility();
                    if (crouch != null && !crouch.equals("none")) {
                        p.sendMessage(ChatColor.GREEN + "Crouch Ability: " + plugin.getAbilityManager().getAbility(crouch).getName());
                    }
                    // show keybinds
                    p.sendMessage(ChatColor.AQUA + "Keybinds: F = primary ability, Shift+F = secondary ability (or use caster items)");
                    p.sendMessage(ChatColor.AQUA + "Simply crouch (hold shift) to trigger any special crouch ability.");
                }
            }
            return true;
        } else if (sub.equals("menu")) {
            if (!(sender instanceof Player)) return true;
            new org.origins.gui.OriginMenu().open((Player) sender);
            return true;
        } else if (sub.equals("evolve")) {
            if (!(sender instanceof Player)) return true;
            Player p = (Player) sender;
            PlayerData data = plugin.getPlayerDataManager().getData(p.getUniqueId());
            String current = data.getOrigin();
            if (current == null) {
                p.sendMessage(ChatColor.RED + "You have no origin to evolve.");
                return true;
            }
            org.origins.origins.Origin o = plugin.getOriginManager().getOrigin(current);
            if (o == null || o.getEvolution() == null) {
                p.sendMessage(ChatColor.RED + "Your origin cannot evolve.");
                return true;
            }
            String target = o.getEvolution();
            int req = plugin.getEvolutionRequirement(current);
            if (data.getTimePlayedWithOrigin() < req) {
                p.sendMessage(ChatColor.RED + "You need " + req + " seconds of playtime to evolve.");
                return true;
            }
            // set origin via plugin helper so attributes update cleanly
            plugin.setPlayerOrigin(p, target);
            p.sendMessage(ChatColor.GREEN + "Congratulations! You have evolved into " + target + "!");
            return true;
        } else if (sub.equals("transform")) {
            if (!(sender instanceof Player)) return true;
            Player p = (Player) sender;
            PlayerData data = plugin.getPlayerDataManager().getData(p.getUniqueId());
            long last = data.getLastTransform();
            long now = System.currentTimeMillis();
            int cd = plugin.getTransformCooldown();
            if (now - last < cd * 1000L) {
                long rem = (cd * 1000L - (now - last)) / 1000;
                p.sendMessage(ChatColor.RED + "You must wait " + rem + "s before transforming again.");
                return true;
            }
            data.setLastTransform(now);
            // placeholder transform logic: disguise as dragon
            plugin.getTransformManager().disguiseAs(p, "enderdragon");
            p.sendMessage(ChatColor.YELLOW + "Transformation triggered.");
            return true;
        } else if (sub.equals("ability")) {
            if (!(sender instanceof Player)) return true;
            Player p = (Player) sender;
            PlayerData data = plugin.getPlayerDataManager().getData(p.getUniqueId());
            String origId = data.getOrigin();
            if (origId == null) {
                p.sendMessage(ChatColor.RED + "You have no origin.");
                return true;
            }
            org.origins.origins.Origin orig = plugin.getOriginManager().getOrigin(origId);
            if (orig == null) return true;
            String abilityId = orig.getPrimaryAbility();
            if (abilityId == null || abilityId.equals("none")) {
                p.sendMessage(ChatColor.RED + "No primary ability.");
                return true;
            }
            plugin.getAbilityManager().activate(p, abilityId);
            return true;
        } else if (sub.equals("set")) {
            if (!sender.hasPermission("origin.admin")) {
                sender.sendMessage(ChatColor.RED + "No permission.");
                return true;
            }
            if (args.length < 3) {
                sender.sendMessage(ChatColor.RED + "Usage: /origin set <player> <origin>");
                return true;
            }
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Player not found.");
                return true;
            }
            String originId = args[2];
            // use plugin helper to set origin (resets previous attributes)
            plugin.setPlayerOrigin(target, originId);
            // reset state values
            PlayerData td = plugin.getPlayerDataManager().getData(target.getUniqueId());
            td.setIntState("hydration", 100);
            td.setIntState("dry_timer", 0);
            td.setIntState("sun_exposure", 0);
            td.setIntState("sun_damage_timer", 0);
            td.setIntState("wing_charge", 0);
            org.origins.bossbar.BossBarManager.removeBar(target);
            sender.sendMessage(ChatColor.GREEN + "Set origin of " + target.getName() + " to " + originId);
            return true;
        } else if (sub.equals("reset")) {
            if (!sender.hasPermission("origin.admin")) {
                sender.sendMessage(ChatColor.RED + "No permission.");
                return true;
            }
            if (args.length < 2) {
                sender.sendMessage(ChatColor.RED + "Usage: /origin reset <player>");
                return true;
            }
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Player not found.");
                return true;
            }
            // reset origin via plugin helper to clear attributes cleanly
            plugin.resetPlayerOrigin(target);
            PlayerData td2 = plugin.getPlayerDataManager().getData(target.getUniqueId());
            // clear states and boss bar
            td2.setIntState("hydration", 0);
            td2.setIntState("dry_timer", 0);
            td2.setIntState("sun_exposure", 0);
            td2.setIntState("sun_damage_timer", 0);
            td2.setIntState("wing_charge", 0);
            org.origins.bossbar.BossBarManager.removeBar(target);
            sender.sendMessage(ChatColor.GREEN + "Reset origin of " + target.getName());
            return true;
        } else if (sub.equals("giveorb")) {
            if (!sender.hasPermission("origin.admin")) {
                sender.sendMessage(ChatColor.RED + "No permission.");
                return true;
            }
            if (args.length < 2) {
                sender.sendMessage(ChatColor.RED + "Usage: /origin giveorb <player> [orb_id]");
                return true;
            }
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Player not found.");
                return true;
            }
            String orbId = "dragon_orb";
            if (args.length >= 3) orbId = args[2];
            ItemStack orb = plugin.getItemManager().getCustomItem(orbId);
            if (orb == null) {
                sender.sendMessage(ChatColor.RED + "Unknown orb id " + orbId);
                return true;
            }
            target.getInventory().addItem(orb);
            sender.sendMessage(ChatColor.GREEN + "Gave " + orbId + " to " + target.getName());
            return true;
        } else if (sub.equals("givecaster")) {
            if (!sender.hasPermission("origin.admin")) {
                sender.sendMessage(ChatColor.RED + "No permission.");
                return true;
            }
            if (args.length < 3) {
                sender.sendMessage(ChatColor.RED + "Usage: /origin givecaster <player> <primary|secondary>");
                return true;
            }
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Player not found.");
                return true;
            }
            String type = args[2].toLowerCase();
            String configPath = "ability_caster." + type + ".name";
            if (!plugin.getConfig().contains(configPath)) {
                sender.sendMessage(ChatColor.RED + "Invalid caster type. Use primary or secondary.");
                return true;
            }
            org.bukkit.Material mat = org.bukkit.Material.valueOf(plugin.getConfig().getString("ability_caster." + type + ".material"));
            String name = org.bukkit.ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString(configPath));
            ItemStack caster = new ItemStack(mat);
            org.bukkit.inventory.meta.ItemMeta meta = caster.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(name);
                caster.setItemMeta(meta);
            }
            target.getInventory().addItem(caster);
            sender.sendMessage(ChatColor.GREEN + "Gave " + type + " caster to " + target.getName());
            return true;
        }
        // TODO: implement other commands
        return true;
    }

    private void handleChoose(Player player) {
        // open GUI
        new org.origins.gui.OriginMenu().open(player);
    }
}

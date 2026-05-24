package org.origins.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.origins.OriginsEvolved;
import org.origins.abilities.Ability;
import org.origins.player.PlayerData;

public class PlayerInteractListener implements Listener {
    private final OriginsEvolved plugin = OriginsEvolved.get();

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        ItemStack item = event.getItem();
        String id = null;
        // find matching custom item by display name
        for (String key : plugin.getItemManager().getItemIds()) {
            ItemStack template = plugin.getItemManager().getCustomItem(key);
            if (template != null && template.hasItemMeta() && item.hasItemMeta()) {
                if (template.getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName())) {
                    id = key;
                    break;
                }
            }
        }
        if (id == null) return;
        event.setCancelled(true);
        Player p = event.getPlayer();
        // if generic ability orb, choose ability based on origin and action
        if (id.equals("ability_orb")) {
            PlayerData data = plugin.getPlayerDataManager().getData(p.getUniqueId());
            String originId = data.getOrigin();
            if (originId == null) return;
            String abilityId = null;
            org.origins.origins.Origin orig = plugin.getOriginManager().getOrigin(originId);
            if (orig == null) return;
            boolean sneak = p.isSneaking();
            if (event.getAction().toString().contains("RIGHT_CLICK")) {
                if (sneak) abilityId = orig.getSecondaryAbility();
                else abilityId = orig.getPrimaryAbility();
            } else if ((event.getAction().toString().contains("LEFT_CLICK")) && sneak) {
                abilityId = orig.getCrouchAbility();
            }
            if (abilityId == null || abilityId.equals("none")) {
                return;
            }
            plugin.getAbilityManager().activate(p, abilityId);
            return;
        }
        // check for caster items (primary and secondary)
        String primMatName = plugin.getConfig().getString("ability_caster.primary.material", "BLAZE_ROD");
        String primName = org.bukkit.ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("ability_caster.primary.name", "&bPrimary Ability Caster"));
        String secMatName = plugin.getConfig().getString("ability_caster.secondary.material", "STICK");
        String secName = org.bukkit.ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("ability_caster.secondary.name", "&aSecondary Ability Caster"));
        org.bukkit.Material primMat = org.bukkit.Material.valueOf(primMatName);
        org.bukkit.Material secMat = org.bukkit.Material.valueOf(secMatName);

        if (item.getType() == primMat && item.hasItemMeta() && item.getItemMeta().hasDisplayName()
                && item.getItemMeta().getDisplayName().equals(primName)) {
            // primary caster always triggers primary (or crouch on sneak+left)
            PlayerData data = plugin.getPlayerDataManager().getData(p.getUniqueId());
            String originId = data.getOrigin();
            if (originId != null) {
                org.origins.origins.Origin orig = plugin.getOriginManager().getOrigin(originId);
                if (orig != null) {
                    String abilityId2 = null;
                    boolean sneak = p.isSneaking();
                    if (event.getAction().toString().contains("RIGHT_CLICK")) {
                        abilityId2 = orig.getPrimaryAbility();
                    } else if (event.getAction().toString().contains("LEFT_CLICK") && sneak) {
                        abilityId2 = orig.getCrouchAbility();
                    }
                    if (abilityId2 != null && !abilityId2.equals("none")) {
                        plugin.getAbilityManager().activate(p, abilityId2);
                    }
                }
            }
            return;
        }
        if (item.getType() == secMat && item.hasItemMeta() && item.getItemMeta().hasDisplayName()
                && item.getItemMeta().getDisplayName().equals(secName)) {
            PlayerData data = plugin.getPlayerDataManager().getData(p.getUniqueId());
            String originId = data.getOrigin();
            if (originId != null) {
                org.origins.origins.Origin orig = plugin.getOriginManager().getOrigin(originId);
                if (orig != null) {
                    if (event.getAction().toString().contains("RIGHT_CLICK")) {
                        String abilityId2 = orig.getSecondaryAbility();
                        if (abilityId2 != null && !abilityId2.equals("none")) {
                            plugin.getAbilityManager().activate(p, abilityId2);
                        }
                    }
                }
            }
            return;
        }
        String abilityId = plugin.getItemManager().getItemAbility(id);
        if (abilityId == null) return;
        plugin.getAbilityManager().activate(p, abilityId);
    }
}
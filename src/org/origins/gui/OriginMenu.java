package org.origins.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.origins.OriginsEvolved;
import org.origins.origins.Origin;

import java.util.ArrayList;
import java.util.List;

public class OriginMenu {
    private final OriginsEvolved plugin = OriginsEvolved.get();
    // keep track of current page per player so arrow clicks work
    private static final java.util.Map<java.util.UUID, Integer> pages = new java.util.HashMap<>();
    // inventory limited to 54 slots (6 rows) by Bukkit
    private static final int INVENTORY_SIZE = 54;
    // use bottom-left and bottom-right slots for navigation
    private static final int LEFT_ARROW_SLOT = 45;
    private static final int RIGHT_ARROW_SLOT = 53;

    public void open(Player player) {
        openPage(player, 1);
    }

    public void openPage(Player player, int page) {
        java.util.List<Origin> originList = new java.util.ArrayList<>(plugin.getOriginManager().getAllOrigins().values());
        int perPage = INVENTORY_SIZE - 2; // leave two slots for arrows
        int totalPages = (int) Math.ceil(originList.size() / (double) perPage);
        if (page < 1) page = 1;
        if (page > totalPages) page = totalPages;
        pages.put(player.getUniqueId(), page);

        Inventory inv = Bukkit.createInventory(null, INVENTORY_SIZE, "Select Origin - " + page + "/" + totalPages);

        // fill origins for this page
        int start = (page - 1) * perPage;
        int end = Math.min(start + perPage, originList.size());
        int slot = 0;
        for (int i = start; i < end; i++) {
            // skip arrow slots
            if (slot == LEFT_ARROW_SLOT) slot++;
            if (slot == RIGHT_ARROW_SLOT) slot++;
            Origin o = originList.get(i);
            org.bukkit.Material mat = o.getIcon() != null ? o.getIcon() : org.bukkit.Material.PAPER;
            ItemStack item = new ItemStack(mat);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.GREEN + o.getDisplayName());
            java.util.List<String> lore = new java.util.ArrayList<>();
            lore.add(ChatColor.GRAY + o.getDescription());
            if (o.getPrimaryAbility() != null && !o.getPrimaryAbility().equals("none")) {
                lore.add(ChatColor.AQUA + "Primary: " + getAbilityName(o.getPrimaryAbility()));
            }
            if (o.getSecondaryAbility() != null && !o.getSecondaryAbility().equals("none")) {
                lore.add(ChatColor.AQUA + "Secondary: " + getAbilityName(o.getSecondaryAbility()));
            }
            if (o.getCrouchAbility() != null && !o.getCrouchAbility().equals("none")) {
                lore.add(ChatColor.AQUA + "Crouch: " + getAbilityName(o.getCrouchAbility()));
            }
            if (o.getEvolution() != null && !o.getEvolution().equals("")) {
                int req = plugin.getEvolutionRequirement(o.getId());
                if (req > 0) {
                    lore.add(ChatColor.YELLOW + "Evolves to " + o.getEvolution() + " after " + (req/60) + "m");
                } else {
                    lore.add(ChatColor.YELLOW + "Evolves to " + o.getEvolution());
                }
            }
            meta.setLore(lore);
            item.setItemMeta(meta);
            inv.setItem(slot, item);
            slot++;
        }

        // arrows
        if (page > 1) {
            ItemStack left = new ItemStack(org.bukkit.Material.ARROW);
            ItemMeta lm = left.getItemMeta();
            lm.setDisplayName(ChatColor.YELLOW + "Previous page");
            left.setItemMeta(lm);
            inv.setItem(LEFT_ARROW_SLOT, left);
        }
        if (page < totalPages) {
            ItemStack right = new ItemStack(org.bukkit.Material.ARROW);
            ItemMeta rm = right.getItemMeta();
            rm.setDisplayName(ChatColor.YELLOW + "Next page");
            right.setItemMeta(rm);
            inv.setItem(RIGHT_ARROW_SLOT, right);
        }

        player.openInventory(inv);
    }

    private String getAbilityName(String abilityId) {
        if (abilityId == null || abilityId.equals("none")) {
            return "None";
        }
        org.origins.abilities.Ability ability = plugin.getAbilityManager().getAbility(abilityId);
        return ability != null ? ability.getName() : abilityId;
    }

    public Integer getPage(Player player) {
        return pages.get(player.getUniqueId());
    }
}


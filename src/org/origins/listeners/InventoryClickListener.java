package org.origins.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.origins.OriginsEvolved;
import org.origins.origins.Origin;
import org.origins.player.PlayerData;
import org.origins.gui.OriginMenu;

public class InventoryClickListener implements Listener {
    private final OriginsEvolved plugin = OriginsEvolved.get();

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        String title = event.getView().getTitle();
        if (title.startsWith("Select Origin")) {
            event.setCancelled(true);
            ItemStack item = event.getCurrentItem();
            if (item == null || !item.hasItemMeta()) return;
            String display = ChatColor.stripColor(item.getItemMeta().getDisplayName());
            // check for arrows
            if (display.equals("Previous page") || display.equals("Next page")) {
                if (!(event.getWhoClicked() instanceof org.bukkit.entity.Player)) return;
                org.bukkit.entity.Player p = (org.bukkit.entity.Player) event.getWhoClicked();
                OriginMenu menu = new OriginMenu();
                int curPage = menu.getPage(p) == null ? 1 : menu.getPage(p);
                int totalPages = 1;
                if (title.contains("/")) {
                    try {
                        totalPages = Integer.parseInt(title.split("/")[1]);
                    } catch (NumberFormatException ignored) {}
                }
                if (display.equals("Previous page")) {
                    menu.openPage(p, Math.max(1, curPage - 1));
                } else {
                    menu.openPage(p, Math.min(totalPages, curPage + 1));
                }
                return;
            }
            // otherwise it should be an origin
            String name = display.toLowerCase().replace(' ', '_');
            Origin o = plugin.getOriginManager().getOrigin(name);
            if (o != null && event.getWhoClicked() instanceof org.bukkit.entity.Player) {
                org.bukkit.entity.Player p = (org.bukkit.entity.Player) event.getWhoClicked();
                // set origin through plugin helper so attributes are reset/applied cleanly
                plugin.setPlayerOrigin(p, name);
                // reset all origin state values
                org.origins.player.PlayerData data = plugin.getPlayerDataManager().getData(p.getUniqueId());
                data.setIntState("hydration", 100);
                data.setIntState("dry_timer", 0);
                data.setIntState("sun_exposure", 0);
                data.setIntState("sun_damage_timer", 0);
                data.setIntState("wing_charge", 0);
                // clear any existing boss bar
                org.origins.bossbar.BossBarManager.removeBar(p);
                p.sendMessage(ChatColor.GREEN + "You are now " + o.getDisplayName());
                p.closeInventory();
            }
        }
    }
}

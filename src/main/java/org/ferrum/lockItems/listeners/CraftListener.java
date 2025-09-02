package org.ferrum.lockItems.listeners;

import org.bukkit.block.Crafter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.CrafterCraftEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.ferrum.lockItems.LockItems;
import org.ferrum.lockItems.utils.ConfigManager;
import org.ferrum.lockItems.utils.DataManager;

import java.util.*;


public class CraftListener implements Listener {

    @EventHandler
    public void onItemCraft(CraftItemEvent event) {

        for (ItemStack item : event.getInventory().getMatrix()) {
            if (DataManager.hasDataFromItem(item, LockItems.lock_owner) && (!Objects.equals(DataManager.getDataFromItem(item, LockItems.lock_owner), event.getWhoClicked().getUniqueId().toString()))) {
                event.setCancelled(true);
                event.getWhoClicked().sendMessage(ConfigManager.getStringByKey("attempt_craft_locked_item", DataManager.getItemOwner(item)));
            }
        }
    }


    @EventHandler
    public void onCrafterCraft(CrafterCraftEvent event) {

        Inventory inventory = ((Crafter) event.getBlock().getState()).getInventory();

        for (ItemStack item : inventory) {
            if (DataManager.hasDataFromItem(item, LockItems.lock_owner)) {
                event.setCancelled(true);
                return;
            }
        }
    }


    @EventHandler
    public void onCartographyTableUse(InventoryClickEvent event) {

        Inventory inv = event.getClickedInventory();

        if (inv != null && inv.getType() == InventoryType.CARTOGRAPHY) {
            ItemStack item = event.getCurrentItem();
            if (DataManager.hasDataFromItem(item, LockItems.lock_owner) && Objects.equals(DataManager.getDataFromItem(item, LockItems.lock_owner), event.getWhoClicked().getUniqueId().toString())) {
                event.setCancelled(true);
                event.getWhoClicked().sendMessage(ConfigManager.getStringByKey("attempt_craft_locked_item", DataManager.getItemOwner(item)));
            }
        }
    }
}
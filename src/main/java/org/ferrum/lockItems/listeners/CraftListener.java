package org.ferrum.lockItems.listeners;


import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.block.Crafter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.CrafterCraftEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.ferrum.lockItems.LockItems;
import org.ferrum.lockItems.utils.ConfigManager;
import org.ferrum.lockItems.utils.DataManager;

import java.util.*;


public class CraftListener implements Listener {


    private final LockItems plugin;
    private final ConfigManager configManager;

    public CraftListener(LockItems plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
    }


    @EventHandler
    public void onItemCraft(CraftItemEvent event) {

        for (ItemStack item : event.getInventory().getMatrix()) {
            if (item != null && plugin.LockableItems.contains(item.getType())) {
                UUID uuidOwner = DataManager.getDataFromItem(item, plugin);
                if (uuidOwner == null) { return; }
                Player owner = Bukkit.getPlayer(uuidOwner);
                if (!Objects.equals(uuidOwner, event.getWhoClicked().getUniqueId())) {
                    event.setCancelled(true);
                    event.getWhoClicked().sendMessage(configManager.getStringByKey("attempt_craft_locked_item", owner));
                }
            }
        }
    }

    @EventHandler
    public void onCrafterCraft(CrafterCraftEvent event) {

        Crafter crafter = (Crafter) event.getBlock().getState();
        Inventory inventory = crafter.getInventory();

        for (ItemStack item : inventory) {
            if (item != null && plugin.LockableItems.contains(item.getType())) {
                if (DataManager.containsItemData(item, plugin)) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }


    @EventHandler
    public void onCartographyTableUse(InventoryClickEvent event) {

        Inventory inv = event.getClickedInventory();

        if (inv != null && inv.getType() == InventoryType.CARTOGRAPHY) {
            ItemStack item = event.getCurrentItem();
            if (item != null && plugin.LockableItems.contains(item.getType())) {
                UUID uuidOwner = DataManager.getDataFromItem(item,plugin);
                if (uuidOwner == null) {return;}
                Player owner = Bukkit.getPlayer(uuidOwner);
                if (!Objects.equals(uuidOwner, event.getWhoClicked().getUniqueId())) {
                    event.setCancelled(true);
                    event.getWhoClicked().sendMessage(configManager.getStringByKey("attempt_craft_locked_item", owner));
                }
            }
        }
    }
}
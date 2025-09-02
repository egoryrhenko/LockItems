package org.ferrum.lockItems.utils;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.ferrum.lockItems.LockItems;

import java.util.UUID;


public class DataManager {

     static public void setDataToItem(ItemStack item, NamespacedKey key, String data) {

         if (!LockItems.LockableItems.contains(item.getType())) {
            return;
         }

         ItemMeta meta = item.getItemMeta();

        if (meta == null) {
            return;
        }

        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, data);
        item.setItemMeta(meta);
    }

    static public String getDataFromItem(ItemStack item, NamespacedKey key) {

        if (!LockItems.LockableItems.contains(item.getType())) {
            return null;
        }

        ItemMeta meta = item.getItemMeta();

        if (meta == null) {
            return null;
        }

        return meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
    }

    static public boolean hasDataFromItem(ItemStack item, NamespacedKey key) {
         if (item == null) {
             return false;
         }

        if (!LockItems.LockableItems.contains(item.getType())) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();

        if (meta == null) {
            return false;
        }

        return meta.getPersistentDataContainer().has(key, PersistentDataType.STRING);
    }

    static public void removeDataFromItem(ItemStack item, NamespacedKey key) {

        if (!LockItems.LockableItems.contains(item.getType())) {
            return;
        }

        ItemMeta meta = item.getItemMeta();

        if (meta == null) {
            return;
        }

        meta.getPersistentDataContainer().remove(key);
        item.setItemMeta(meta);
    }

    static public OfflinePlayer getItemOwner(ItemStack item) {

         String uuid = getDataFromItem(item, LockItems.lock_owner);

         if (uuid == null) {
             return null;
         }

        return Bukkit.getOfflinePlayer(UUID.fromString(uuid));
    }
}

package org.ferrum.lockItems.utils;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.ferrum.lockItems.LockItems;

import java.util.UUID;

public class DataManager {

     static public void setDataToItem(ItemStack item, NamespacedKey key, String data) {
        ItemMeta meta = item.getItemMeta();

        if (meta == null) {
            return;
        }

        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, data);
        item.setItemMeta(meta);
    }

    static public String getDataFromItem(ItemStack item, NamespacedKey key) {
        ItemMeta meta = item.getItemMeta();

        if (meta == null) {
            return null;
        }

        return meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
    }

    static public boolean hasDataFromItem(ItemStack item, NamespacedKey key) {
        ItemMeta meta = item.getItemMeta();

        if (meta == null) {
            return false;
        }

        return meta.getPersistentDataContainer().has(key, PersistentDataType.STRING);
    }

    static public void removeDataFromItem(ItemStack item, NamespacedKey key) {
        ItemMeta meta = item.getItemMeta();

        if (meta == null) {
            return;
        }

        meta.getPersistentDataContainer().remove(key);
        item.setItemMeta(meta);
    }
}

package org.ferrum.lockItems.utils;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class DataManager {

     static public void addDataToItem(ItemStack item, UUID data, JavaPlugin plugin) {
        ItemMeta meta = item.getItemMeta();

        if (meta == null) {
            return;
        }

        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
        NamespacedKey dataKey = new NamespacedKey(plugin, "lock_owner");

        dataContainer.set(dataKey, PersistentDataType.STRING, data.toString());
        item.setItemMeta(meta);
    }

    static public UUID getDataFromItem(ItemStack item, JavaPlugin plugin) {
        ItemMeta meta = item.getItemMeta();

        if (meta == null) {
            return null;
        }

        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
        NamespacedKey dataKey = new NamespacedKey(plugin, "lock_owner");

        String data = dataContainer.getOrDefault(dataKey, PersistentDataType.STRING, "");

        if (data.isEmpty()){
            return null;
        }
        return UUID.fromString(data);
    }

    static public void removeDataFromItem(ItemStack item, JavaPlugin plugin) {
        ItemMeta meta = item.getItemMeta();

        if (meta == null) {
            return;
        }

        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
        NamespacedKey dataKey = new NamespacedKey(plugin, "lock_owner");

        // Удаляем данные по ключу
        dataContainer.remove(dataKey);

        item.setItemMeta(meta);
    }
}

package org.ferrum.lockItems.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.ferrum.lockItems.LockItems;

import java.io.File;
import java.util.*;
import java.util.logging.Level;

public class ConfigManager {
    private static FileConfiguration config;
    private static FileConfiguration messages;
    private static boolean PlaceholderAPI_isLoad;

    public static void init() {
        loadConfig();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            PlaceholderAPI_isLoad = true;
        } else {
            LockItems.plugin.getLogger().log(Level.INFO, "PlaceholderAPI not found!");
        }
    }

    public static void loadConfig() {
        File configFile = new File(LockItems.plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            LockItems.plugin.saveResource("config.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(configFile);

        File languageFile = new File(LockItems.plugin.getDataFolder() + "/language/"+config.getString("language","en") + ".yml");
        if (!languageFile.exists()) {
            try {
                LockItems.plugin.saveResource("language/"+config.getString("language","en") + ".yml", false);
            } catch (IllegalArgumentException e) {
                LockItems.plugin.getLogger().warning(config.getString("language")+".yml not found!");
            }
        }
        messages = YamlConfiguration.loadConfiguration(languageFile);

        updateLockItems();
    }

    public static String getStringByKey(String key, OfflinePlayer player) {
        String string = messages.getString(key, "text."+key);

        if (player != null && player.getName() != null) {
            string = string.replace("{Player}", player.getName());
        } else {
            string = string.replace("{Player}","");
        }

        if (PlaceholderAPI_isLoad) {
            string = PlaceholderAPI.setPlaceholders(player, string);
        }
        string = ChatColor.translateAlternateColorCodes('&', string);

        return string;
    }

    public static String getStringByKey(String key) {
        return ChatColor.translateAlternateColorCodes('&', messages.getString(key, "text."+key));
    }

    public static void updateLockItems() {
        LockItems.LockableItems.clear();
        List<String> LockItemsList = config.getStringList("ItemCanBeLock");
        List<String> LockItemsTagList = config.getStringList("ItemTagCanBeLock");

        for (String material : LockItemsList) {
            LockItems.LockableItems.add( Material.getMaterial(material) );
        }

        for (String lockItemTagName : LockItemsTagList) {
            Tag<Material> itemsFoTag = Bukkit.getTag(Tag.REGISTRY_ITEMS, NamespacedKey.minecraft(lockItemTagName), Material.class);
            if (itemsFoTag != null) {
                LockItems.LockableItems.addAll(itemsFoTag.getValues());
            }
        }

        LockItems.plugin.getLogger().log(Level.INFO, "Register "+LockItems.LockableItems.size()+" items");
    }
}
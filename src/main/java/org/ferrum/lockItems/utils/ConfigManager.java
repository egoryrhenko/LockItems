package org.ferrum.lockItems.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.ferrum.lockItems.LockItems;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class ConfigManager {
    private static LockItems plugin;
    private static FileConfiguration config;
    private static FileConfiguration messages;
    private static boolean PlaceholderAPI_isLoad;

    public ConfigManager(LockItems plugin) {
        this.plugin = plugin;
        loadConfig();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            PlaceholderAPI_isLoad = true;
        } else {
            plugin.getLogger().log(Level.INFO, "PlaceholderAPI not found!");
        }
    }

    public static void loadConfig() {
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(configFile);

        File languageFile = new File(plugin.getDataFolder() + "/language/"+config.getString("language","en") + ".yml");
        if (!languageFile.exists()) {
            try {
                plugin.saveResource("language/"+config.getString("language","en") + ".yml", false);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning(config.getString("language")+".yml not found!");
            }
        }
        messages = YamlConfiguration.loadConfiguration(languageFile);

        updateLockItems();
    }

    public static String getStringByKey(String key, OfflinePlayer player) {
        String string = messages.getString(key, "text."+key);
        string = string.replace("{Player}", player.getName());
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
        plugin.LockableItems.clear();
        List<String> LockItems = config.getStringList("ItemCanBeLock");
        List<String> LockItemsTag = config.getStringList("ItemTagCanBeLock");

        for (String material : LockItems) {
            plugin.LockableItems.add( Material.getMaterial(material) );
        }

        for (String lockItemTagName : LockItemsTag) {
            Tag<Material> itemsFoTag = Bukkit.getTag(Tag.REGISTRY_ITEMS, NamespacedKey.minecraft(lockItemTagName), Material.class);
            if (itemsFoTag != null) {
                plugin.LockableItems.addAll(itemsFoTag.getValues());
            }
        }

        plugin.getLogger().log(Level.INFO, "Register "+plugin.LockableItems.size()+" items");
    }
}
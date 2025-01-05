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
    private LockItems plugin;
    private FileConfiguration config;
    private FileConfiguration messages;
    private boolean PlaceholderAPI_isLoad;

    public ConfigManager(LockItems plugin) {
        this.plugin = plugin;
        loadConfig();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            PlaceholderAPI_isLoad = true;
        } else {
            plugin.getLogger().log(Level.INFO, "PlaceholderAPI not found!");
        }
    }

    public void loadConfig() {
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

    public String getStringByKey(String key, Player player) {
        String string = messages.getString(key, "text."+key);
        string = string.replace("{Player}", player.getName());
        if (PlaceholderAPI_isLoad) {
            string = PlaceholderAPI.setPlaceholders(player, string);
        }
        string = ChatColor.translateAlternateColorCodes('&', string);

        return string;
    }

    public String getStringByKey(String key) {
        String string = messages.getString(key, "text."+key);
        string = ChatColor.translateAlternateColorCodes('&', string);

        return string;
    }



    public void updateLockItems() {
        HashSet<Material> lockItems  = new HashSet<>();
        List<?> LockItems = config.getList("ItemCanBeLock");
        List<?> LockItemsTag = config.getList("ItemTagCanBeLock");

        if (LockItems != null) {
            for (Object obj : LockItems) {
                if (obj instanceof String lockItemName){
                    lockItems.add( Material.getMaterial(lockItemName));
                }
            }
        }
        if (LockItemsTag != null) {
            for (Object obj : LockItemsTag) {
                if (obj instanceof String lockItemTagName){
                    Tag<Material> itemsFoTag = Bukkit.getTag(Tag.REGISTRY_ITEMS, NamespacedKey.minecraft(lockItemTagName), Material.class);
                    if (itemsFoTag  != null){
                        lockItems.addAll(itemsFoTag.getValues());
                    }
                }
            }
        }
        plugin.getLogger().log(Level.INFO, "Register "+lockItems.size()+" items");
        plugin.LockableItems = lockItems;
    }
}

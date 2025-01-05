package org.ferrum.lockItems.commands;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.ferrum.lockItems.LockItems;
import org.ferrum.lockItems.utils.ConfigManager;

import java.util.ArrayList;
import java.util.List;

import static org.ferrum.lockItems.utils.DataManager.*;

public class LockCommand implements CommandExecutor {

    private final LockItems plugin;
    private final ConfigManager configManager;
    public LockCommand(LockItems plugin){
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {

            ItemStack itemInHand = player.getInventory().getItemInMainHand();

            if (getDataFromItem(itemInHand, plugin) != null){
                player.sendMessage(configManager.getStringByKey("item_already_locked", player));
                return true;
            }

            if (plugin.LockableItems.contains(itemInHand.getType())) {

                addDataToItem(itemInHand, player.getUniqueId(), plugin);

                ItemMeta meta = itemInHand.getItemMeta();
                List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
                String item_lore = configManager.getStringByKey("item_lore").replace("{Player}", player.getName());
                if (!item_lore.isEmpty() && lore != null){
                    lore.add(ChatColor.RESET + item_lore);
                }
                meta.setLore(lore);
                itemInHand.setItemMeta(meta);

                player.sendMessage(configManager.getStringByKey("item_successfully_locked", player));
                return true;
            } else if (itemInHand.getType().isAir()){
                player.sendMessage(configManager.getStringByKey("attempt_lock_empty_hand", player));
                return true;
            } else {
                player.sendMessage(configManager.getStringByKey("attempt_lock_item_not_in_list", player));
                return true;
            }
        } else {
            sender.sendMessage(configManager.getStringByKey("command_sender_not_player"));
            return true;
        }
    }
}

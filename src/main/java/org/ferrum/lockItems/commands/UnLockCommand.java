package org.ferrum.lockItems.commands;

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
import java.util.UUID;

import static org.ferrum.lockItems.utils.DataManager.*;

public class UnLockCommand implements CommandExecutor {

    private final LockItems plugin;
    private final ConfigManager configManager;

    public UnLockCommand(LockItems plugin){
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {

            ItemStack itemInHand = player.getInventory().getItemInMainHand();

            if (plugin.LockableItems.contains(itemInHand.getType())) {

                UUID uuidOwner = getDataFromItem(itemInHand,plugin);

                if (uuidOwner == null){

                    return true;
                }

                if (uuidOwner.equals(player.getUniqueId())){
                    removeDataFromItem(itemInHand, plugin);

                    ItemMeta meta = itemInHand.getItemMeta();
                    List<String> lore = meta.getLore();

                    String lockLore = configManager.getStringByKey("item_lore").replace("{Player}", player.getName());

                    lore.remove(lockLore);

                    meta.setLore(lore);
                    itemInHand.setItemMeta(meta);

                    player.sendMessage(configManager.getStringByKey("item_successfully_unlocked", player));
                    return true;
                } else {
                    player.sendMessage(configManager.getStringByKey("attempt_unlock_item_not_owner", player));
                    return true;
                }
            } else if (itemInHand.getType().isAir()){
                player.sendMessage(configManager.getStringByKey("attempt_unlock_empty_hand", player));
                return true;
            } else {
                player.sendMessage(configManager.getStringByKey("attempt_unlock_item_not_in_list", player));
                return true;
            }
        }
        return false;
    }
}

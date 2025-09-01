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


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {

            ItemStack itemInHand = player.getInventory().getItemInMainHand();

            if (itemInHand.getType().isAir()){
                player.sendMessage(ConfigManager.getStringByKey("attempt_unlock_empty_hand", player));
                return true;
            }

            if (!LockItems.LockableItems.contains(itemInHand.getType())) {
                player.sendMessage(ConfigManager.getStringByKey("attempt_unlock_item_not_in_list", player));
                return true;
            }

            String uuidOwner = getDataFromItem(itemInHand, LockItems.lock_owner);

            if (uuidOwner == null) {
                player.sendMessage(ConfigManager.getStringByKey("item_not_locked", player));
                return true;
            }

            if (!uuidOwner.equals(player.getUniqueId().toString())) {
                player.sendMessage(ConfigManager.getStringByKey("attempt_unlock_item_not_owner", player));
                return true;
            }

            ItemMeta meta = itemInHand.getItemMeta();
            List<String> lore = meta.getLore();

            if (lore == null) {
                lore = new ArrayList<>();
            }

            if (hasDataFromItem(itemInHand, LockItems.lock_text)) {
                lore.remove(getDataFromItem(itemInHand, LockItems.lock_text));
            } else {
                lore.remove(ConfigManager.getStringByKey("item_lore").replace("{Player}", player.getName()));
            }

            meta.setLore(lore);
            itemInHand.setItemMeta(meta);

            removeDataFromItem(itemInHand, LockItems.lock_owner);
            removeDataFromItem(itemInHand, LockItems.lock_text);

            player.sendMessage(ConfigManager.getStringByKey("item_successfully_unlocked", player));
        } else {
            sender.sendMessage(ConfigManager.getStringByKey("command_sender_not_player"));
        }
        return true;
    }
}

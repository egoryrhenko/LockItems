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

import static org.ferrum.lockItems.utils.DataManager.*;

public class LockCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {

            ItemStack itemInHand = player.getInventory().getItemInMainHand();

            if (hasDataFromItem(itemInHand, LockItems.lock_owner)){
                player.sendMessage(ConfigManager.getStringByKey("item_already_locked", player));
                return true;
            }

            if (LockItems.LockableItems.contains(itemInHand.getType())) {
                ItemMeta meta = itemInHand.getItemMeta();
                List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
                String item_lore = ConfigManager.getStringByKey("item_lore").replace("{Player}", player.getName());
                if (!item_lore.isEmpty() && lore != null){
                    lore.add(ChatColor.RESET + item_lore);
                }
                meta.setLore(lore);
                itemInHand.setItemMeta(meta);

                setDataToItem(itemInHand, LockItems.lock_owner, player.getUniqueId().toString());
                setDataToItem(itemInHand, LockItems.lock_text, item_lore);

                player.sendMessage(ConfigManager.getStringByKey("item_successfully_locked", player));
                return true;
            } else if (itemInHand.getType().isAir()){
                player.sendMessage(ConfigManager.getStringByKey("attempt_lock_empty_hand", player));
                return true;
            } else {
                player.sendMessage(ConfigManager.getStringByKey("attempt_lock_item_not_in_list", player));
                return true;
            }
        } else {
            sender.sendMessage(ConfigManager.getStringByKey("command_sender_not_player"));
            return true;
        }
    }
}

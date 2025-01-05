package org.ferrum.lockItems.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.ferrum.lockItems.LockItems;
import org.ferrum.lockItems.utils.ConfigManager;

import java.util.List;
import java.util.UUID;

import static org.ferrum.lockItems.utils.DataManager.getDataFromItem;
import static org.ferrum.lockItems.utils.DataManager.removeDataFromItem;

public class ForceUnLockCommand implements CommandExecutor {

    private final LockItems plugin;
    private final ConfigManager configManager;

    public ForceUnLockCommand(LockItems plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {

            ItemStack itemInHand = player.getInventory().getItemInMainHand();

            if (plugin.LockableItems.contains(itemInHand.getType())) {

                UUID uuidOwner = getDataFromItem(itemInHand, plugin);

                if (uuidOwner == null) {
                    player.sendMessage(configManager.getStringByKey("item_not_locked", player));
                    return true;
                }

                removeDataFromItem(itemInHand, plugin);

                ItemMeta meta = itemInHand.getItemMeta();
                List<String> lore = meta.getLore();

                Player Owner = Bukkit.getPlayer(uuidOwner);

                if (Owner != null) {
                    String lockLore = configManager.getStringByKey("item_lore").replace("{Player}", Owner.getName());
                    lore.remove(lockLore);
                }

                meta.setLore(lore);
                itemInHand.setItemMeta(meta);

                player.sendMessage(configManager.getStringByKey("item_successfully_unlocked",player));
                return true;
            } else if (itemInHand.getType().isAir()) {
                player.sendMessage(configManager.getStringByKey("attempt_unlock_empty_hand",player));
                return true;
            } else {
                player.sendMessage(configManager.getStringByKey("attempt_unlock_item_not_in_list",player));
                return true;
            }
        } else {
            sender.sendMessage(configManager.getStringByKey("command_sender_not_player"));
            return true;
        }
    }
}
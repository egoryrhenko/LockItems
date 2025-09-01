package org.ferrum.lockItems.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.ferrum.lockItems.utils.ConfigManager;

public class ReloadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ConfigManager.loadConfig();
        sender.sendMessage(ConfigManager.getStringByKey("reload_config"));
        return true;
    }
}

package org.ferrum.lockItems.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.ferrum.lockItems.utils.ConfigManager;

public class ReloadCommand implements CommandExecutor {

    private final ConfigManager configManager;

    public ReloadCommand(ConfigManager configManager){
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        configManager.loadConfig();
        sender.sendMessage(configManager.getStringByKey("reload_config"));
        return true;
    }
}

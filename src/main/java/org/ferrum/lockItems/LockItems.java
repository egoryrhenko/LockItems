package org.ferrum.lockItems;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;
import org.ferrum.lockItems.commands.ForceUnLockCommand;
import org.ferrum.lockItems.commands.LockCommand;
import org.ferrum.lockItems.commands.ReloadCommand;
import org.ferrum.lockItems.commands.UnLockCommand;
import org.ferrum.lockItems.listeners.CraftListener;
import org.ferrum.lockItems.tabCompleters.EmptyTabCompleter;
import org.ferrum.lockItems.utils.ConfigManager;


import java.util.HashSet;

public final class LockItems extends JavaPlugin {

    public HashSet<Material> LockableItems = new HashSet<>();
    private ConfigManager configManager;

    @Override
    public void onEnable() {

        configManager = new ConfigManager(this);
        LockCommand lockCommand = new LockCommand(this);
        UnLockCommand unLockCommand = new UnLockCommand(this);
        ForceUnLockCommand forceUnLockCommand = new ForceUnLockCommand(this);

        EmptyTabCompleter emptyTabCompleter = new EmptyTabCompleter();
        CraftListener craftListener = new CraftListener(this);

        getServer().getPluginManager().registerEvents(craftListener,this);

        RegisterCommand("lock", lockCommand, emptyTabCompleter,"lockitems.lock");
        RegisterCommand("unlock", unLockCommand, emptyTabCompleter,"lockitems.unlock");
        RegisterCommand("forceunlock", forceUnLockCommand, emptyTabCompleter,"lockitems.admin.unlock");
        RegisterCommand("lockitemsreload",new ReloadCommand(configManager),null,"lockitems.reload");
    }

    private void RegisterCommand(String name, CommandExecutor commandExecutor, TabCompleter tabCompleter, String permission ){
        PluginCommand command = getCommand(name);
        if (command == null) {
            return;
        }
        command.setExecutor(commandExecutor);
        command.setTabCompleter(tabCompleter);
        if (permission != null){
            command.setPermission(permission);
        }
    }

    public ConfigManager getConfigManager(){
        return configManager;
    }

}

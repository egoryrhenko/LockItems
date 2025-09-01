package org.ferrum.lockItems;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
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
import org.ferrum.lockItems.utils.DataManager;


import java.util.HashSet;

public final class LockItems extends JavaPlugin {

    public static LockItems plugin;
    public static NamespacedKey lock_owner;
    public static NamespacedKey lock_text;
    public static HashSet<Material> LockableItems = new HashSet<>();

    @Override
    public void onEnable() {
        plugin = this;

        lock_owner = new NamespacedKey(this, "lock_owner");
        lock_text = new NamespacedKey(this, "lock_text");

        ConfigManager.init();
        ReloadCommand reloadCommand = new ReloadCommand();

        LockCommand lockCommand = new LockCommand();
        UnLockCommand unLockCommand = new UnLockCommand();
        ForceUnLockCommand forceUnLockCommand = new ForceUnLockCommand();

        EmptyTabCompleter emptyTabCompleter = new EmptyTabCompleter();
        CraftListener craftListener = new CraftListener();

        getServer().getPluginManager().registerEvents(craftListener,this);

        RegisterCommand("lock", lockCommand, emptyTabCompleter,"lockitems.lock");
        RegisterCommand("unlock", unLockCommand, emptyTabCompleter,"lockitems.unlock");
        RegisterCommand("forceunlock", forceUnLockCommand, emptyTabCompleter,"lockitems.unlock.admin");
        RegisterCommand("lockitemsreload", reloadCommand, emptyTabCompleter,"lockitems.reload");
    }

    private void RegisterCommand(String name, CommandExecutor commandExecutor, TabCompleter tabCompleter, String permission ) {
        PluginCommand command = getCommand(name);
        if (command == null) {
            return;
        }
        command.setExecutor(commandExecutor);
        command.setTabCompleter(tabCompleter);
        if (permission != null) {
            command.setPermission(permission);
        }
    }

}

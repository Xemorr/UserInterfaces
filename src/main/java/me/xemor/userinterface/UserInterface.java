package me.xemor.userinterface;

import me.xemor.userinterface.commands.BookInterfaceCmd;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;

@SuppressWarnings("unused")
public final class UserInterface {
    private static JavaPlugin plugin;
    private static BukkitAudiences bukkitAudiences;
    private static boolean hasFloodgate;

    public static void enable(JavaPlugin plugin) {
        UserInterface.plugin = plugin;

        if (plugin.getServer().getPluginManager().getPlugin("ProtocolLib") == null) {
            throw new IllegalStateException(plugin.getName() + " requires ProtocolLib to run.");
        }

        bukkitAudiences = BukkitAudiences.create(plugin);
        ChestHandler chestHandler = new ChestHandler();

        hasFloodgate = plugin.getServer().getPluginManager().getPlugin("Floodgate") != null;

        registerCommand("ufbook", new BookInterfaceCmd("ufbook"));
        plugin.getServer().getPluginManager().registerEvents(chestHandler, plugin);
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }

    public static BukkitAudiences getBukkitAudiences() {
        return bukkitAudiences;
    }

    public static boolean hasFloodgate() {
        return hasFloodgate;
    }

    private static void registerCommand(String name, Command command) {
        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");

            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

            commandMap.register(name, command);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}

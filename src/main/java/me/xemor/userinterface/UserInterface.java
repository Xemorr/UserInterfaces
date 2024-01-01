package me.xemor.userinterface;

import me.xemor.userinterface.commands.BookInterfaceCmd;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public final class UserInterface {
    private static JavaPlugin plugin;
    private static BukkitAudiences bukkitAudiences;
    private static boolean hasFloodgate;

    public static void enable(JavaPlugin plugin) {
        UserInterface.plugin = plugin;

        bukkitAudiences = BukkitAudiences.create(plugin);
        ChestHandler chestHandler = new ChestHandler();

        hasFloodgate = plugin.getServer().getPluginManager().getPlugin("Floodgate") != null;

        plugin.getCommand("ufbook").setExecutor(new BookInterfaceCmd());
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
}

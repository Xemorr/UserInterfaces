package me.xemor.userinterface;

import me.xemor.userinterface.chestinterface.ChestHandler;
import me.xemor.userinterface.commands.BookInterfaceCmd;
import me.xemor.userinterface.packethandler.PacketHandler;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;

@SuppressWarnings("unused")
public final class UserInterface {
    private static JavaPlugin plugin;
    private static boolean hasFloodgate;
    private static PacketHandler packetHandler;
    private static NamespacedKey slotName;

    public static void enable(JavaPlugin plugin) {
        UserInterface.plugin = plugin;

        ChestHandler chestHandler = new ChestHandler();

        hasFloodgate = plugin.getServer().getPluginManager().getPlugin("Floodgate") != null;

        packetHandler = new PacketHandler();
        slotName = new NamespacedKey(UserInterface.getPlugin(), "slotName");

        registerCommand("ufbook", new BookInterfaceCmd("ufbook"));
        plugin.getServer().getPluginManager().registerEvents(chestHandler, plugin);
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }

    public static boolean hasFloodgate() {
        return hasFloodgate;
    }

    public static PacketHandler getPacketHandler() {
        return packetHandler;
    }

    public static NamespacedKey getSlotNameKey() {
        return slotName;
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

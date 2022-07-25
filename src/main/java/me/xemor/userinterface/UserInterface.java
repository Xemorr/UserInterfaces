package me.xemor.userinterface;

import org.bukkit.plugin.java.JavaPlugin;

public final class UserInterface extends JavaPlugin {

    private static UserInterface userInterface;
    private static boolean hasFloodgate;

    @Override
    public void onEnable() {
        // Plugin startup logic
        userInterface = this;
        ChestHandler chestHandler = new ChestHandler();

        if (this.getServer().getPluginManager().getPlugin("Floodgate") != null) hasFloodgate= true;
        else {
            hasFloodgate = false;
            getLogger().info("Floodgate plugin not found. Continuing without Floodgate.");
        }

        this.getServer().getPluginManager().registerEvents(chestHandler, this);
    }

    public static UserInterface getInstance() {
        return userInterface;
    }

    public static boolean hasFloodgate() { return hasFloodgate; }
}

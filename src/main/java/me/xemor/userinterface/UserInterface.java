package me.xemor.userinterface;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;

public final class UserInterface extends JavaPlugin {

    private static UserInterface userInterface;
    private static boolean hasFloodgate;
    private static BukkitAudiences bukkitAudiences;

    @Override
    public void onEnable() {
        // Plugin startup logic
        userInterface = this;
        bukkitAudiences = BukkitAudiences.create(this);
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

    public static BukkitAudiences getBukkitAudiences() { return bukkitAudiences; }
}

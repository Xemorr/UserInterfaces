package me.xemor.userinterface;

import org.bukkit.plugin.java.JavaPlugin;

public final class UserInterface extends JavaPlugin {

    private static UserInterface userInterface;

    @Override
    public void onEnable() {
        // Plugin startup logic
        userInterface = this;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static UserInterface getInstance() {
        return userInterface;
    }
}

package me.xemor.userinterface.packethandler;

import me.xemor.userinterface.SignMenuFactory.SignMenuFactory;
import me.xemor.userinterface.packethandler.hook.PacketEventsHook;
import me.xemor.userinterface.packethandler.hook.PacketHook;
import me.xemor.userinterface.packethandler.hook.ProtocolLibHook;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

public class PacketHandler {
    private final PacketHook hook;

    public PacketHandler() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        if (pluginManager.getPlugin("packetevents") != null) {
            this.hook = new PacketEventsHook();
        } else if (pluginManager.getPlugin("ProtocolLib") != null) {
            this.hook = new ProtocolLibHook();
        } else {
            throw new IllegalStateException("packetevents or ProtocolLib is required to use this feature");
        }
    }

    public void sendUpdateSignPacket(Player player, Location location, String[] signLines, boolean isFrontText) {
        this.hook.sendUpdateSignPacket(player, location, signLines, isFrontText);
    }

    public void registerSignMenuListener(SignMenuFactory factory) {
        this.hook.registerSignMenuListener(factory);
    }
}

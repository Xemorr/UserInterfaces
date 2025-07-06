package me.xemor.userinterface.packethandler.hook;

import me.xemor.userinterface.SignMenuFactory.SignMenuFactory;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface PacketHook {

    void sendUpdateSignPacket(Player player, Location location, String[] signLines, boolean isFrontText);

    void registerSignMenuListener(SignMenuFactory factory);
}

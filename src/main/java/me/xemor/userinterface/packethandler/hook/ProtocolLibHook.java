package me.xemor.userinterface.packethandler.hook;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import me.xemor.userinterface.SignMenuFactory.SignMenuFactory;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class ProtocolLibHook implements PacketHook {

    @Override
    public void sendUpdateSignPacket(Player player, Location location, String[] signLines, boolean isFrontText) {
        PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.OPEN_SIGN_EDITOR);
        BlockPosition position = new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        packet.getBlockPositionModifier().write(0, position);

        sendPacket(player, packet);
    }

    @Override
    public void registerSignMenuListener(SignMenuFactory factory) {
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(factory.getPlugin(), PacketType.Play.Client.UPDATE_SIGN) {

            @Override
            public void onPacketReceiving(PacketEvent event) {
                Player player = event.getPlayer();
                SignMenuFactory.Menu menu = factory.getInputs().remove(player);
                if (menu == null) {
                    return;
                }

                event.setCancelled(true);

                PacketContainer packet = event.getPacket();
                boolean success = menu.testResponse(player, packet.getStringArrays().read(0));
                if (!success && menu.shouldReopenIfFail() && !menu.shouldForceClose()) {
                    Bukkit.getScheduler().runTaskLater(plugin, () -> menu.open(player), 2L);
                }

                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    if (player.isOnline()) {
                        Location location = menu.getLocation();
                        player.sendBlockChange(location, location.getBlock().getBlockData());
                    }
                }, 2L);
            }
        });
    }

    private void sendPacket(Player player, PacketContainer packet) {
        ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
    }
}

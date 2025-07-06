package me.xemor.userinterface.packethandler.hook;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientUpdateSign;
import me.xemor.userinterface.SignMenuFactory.SignMenuFactory;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PacketEventsHook implements PacketHook {

    @Override
    public void sendUpdateSignPacket(Player player, Location location, String[] signLines, boolean isFrontText) {
        Vector3i position = new Vector3i(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        sendPacket(player, new WrapperPlayClientUpdateSign(position, signLines, true));
    }

    @Override
    public void registerSignMenuListener(SignMenuFactory factory) {
        PacketEvents.getAPI().getEventManager().registerListener(new PacketListenerAbstract() {

            @Override
            public void onPacketReceive(PacketReceiveEvent event) {
                if (event.getPacketType() != PacketType.Play.Server.UPDATE_SIGN) {
                    return;
                }

                Player player = event.getPlayer();
                SignMenuFactory.Menu menu = factory.getInputs().remove(player);
                if (menu == null) {
                    return;
                }

                event.setCancelled(true);

                WrapperPlayClientUpdateSign packet = new WrapperPlayClientUpdateSign(event);
                boolean success = menu.testResponse(player, packet.getTextLines());
                if (!success && menu.shouldReopenIfFail() && !menu.shouldForceClose()) {
                    Bukkit.getScheduler().runTaskLater(factory.getPlugin(), () -> menu.open(player), 2L);
                }

                Bukkit.getScheduler().runTaskLater(factory.getPlugin(), () -> {
                    if (player.isOnline()) {
                        Location location = menu.getLocation();
                        player.sendBlockChange(location, location.getBlock().getBlockData());
                    }
                }, 2L);
            }
        });
    }

    private void sendPacket(Player player, PacketWrapper<?> packet) {
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
    }
}

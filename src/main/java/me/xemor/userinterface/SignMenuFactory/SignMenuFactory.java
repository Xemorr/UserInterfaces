package me.xemor.userinterface.SignMenuFactory;

import me.xemor.userinterface.UserInterface;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiPredicate;

@SuppressWarnings("unused")
public final class SignMenuFactory {
    private final Plugin plugin;
    private final Map<Player, Menu> inputs;

    public SignMenuFactory(Plugin plugin) {
        this.plugin = plugin;
        this.inputs = new HashMap<>();

        UserInterface.getPacketHandler().ifPresent(packetHandler -> packetHandler.registerSignMenuListener(this));
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public Map<Player, Menu> getInputs() {
        return inputs;
    }

    public Menu newMenu(List<String> text) {
        return new Menu(text);
    }

    public final class Menu {
        private final List<String> text;
        private BiPredicate<Player, String[]> response;
        private boolean reopenIfFail;
        private Location location;
        private boolean forceClose;

        Menu(List<String> text) {
            this.text = text;
        }

        public boolean shouldReopenIfFail() {
            return reopenIfFail;
        }

        public Location getLocation() {
            return location;
        }

        public boolean shouldForceClose() {
            return forceClose;
        }

        public Menu reopenIfFail(boolean value) {
            this.reopenIfFail = value;
            return this;
        }

        public boolean testResponse(Player player, String[] text) {
            return this.response.test(player, text);
        }

        public Menu response(BiPredicate<Player, String[]> response) {
            this.response = response;
            return this;
        }

        public void open(Player player) {
            Objects.requireNonNull(player, "player");
            if (!player.isOnline()) {
                return;
            }
            location = player.getLocation();
            location.setY(location.getBlockY() - 4);

            player.sendBlockChange(location, Material.OAK_SIGN.createBlockData());
            String[] signLines = text.stream().map(this::color).toArray(String[]::new);
            player.sendSignChange(location, signLines);

            UserInterface.getPacketHandler().ifPresent(packetHandler -> packetHandler.sendUpdateSignPacket(player, location, signLines, true));
            inputs.put(player, this);
        }

        /**
         * closes the menu. if force is true, the menu will close and will ignore the reopen
         * functionality. false by default.
         *
         * @param player the player
         * @param force  decides whether it will reopen if reopen is enabled
         */
        public void close(Player player, boolean force) {
            this.forceClose = force;
            if (player.isOnline()) {
                player.closeInventory();
            }
        }

        public void close(Player player) {
            close(player, false);
        }

        private String color(String input) {
            return ChatColor.translateAlternateColorCodes('&', input);
        }
    }
}
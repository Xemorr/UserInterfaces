package me.xemor.userinterface.commands;

import me.xemor.userinterface.BookInterface;
import me.xemor.userinterface.UserInterface;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class BookInterfaceCmd extends Command {

    private static final HashMap<String, BookInterface.Button> buttons = new HashMap<>();

    public BookInterfaceCmd(@NotNull String name) {
        super(name);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Console cannot run this command!");
            return true;
        }

        if (args.length != 3) {
            sender.sendMessage("An error has occurred!");
            return true;
        }

        String pluginName = args[0];
        String uuidInput = args[1];
        String cmdIdentifier = args[2];
        if (UserInterface.getPlugin().getName().equalsIgnoreCase(pluginName) || !uuidInput.equals(player.getUniqueId().toString())) {
            return true;
        }

        BookInterface.Button thisButton = buttons.get(cmdIdentifier);
        if (thisButton == null) {
            return true;
        }

        thisButton.runnable().run();
        return true;
    }

    public static void addButton(String identifier, BookInterface.Button button) {
        buttons.put(identifier, button);
        if (buttons.size() > 500) {
            for (int i = 0; i < 10; i++) {
                UserInterface.getPlugin().getLogger().severe("UserInterface Buttons have exceeded 500, this is likely due to mass BookInterface creation");
            }
        }
    }

    public static void removeButton(String identifier) {
        buttons.remove(identifier);
    }

    public static boolean hasButtonIdentifier(String identifier) {
        return buttons.containsKey(identifier);
    }
}

package me.xemor.userinterface.commands;

import me.xemor.userinterface.BookInterface;
import me.xemor.userinterface.UserInterface;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class BookInterfaceCmd implements CommandExecutor {

    private static final HashMap<String, BookInterface.Button> buttons = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage("Console cannot run this command!");
            return true;
        }
        if (args.length != 2) {
            commandSender.sendMessage("An error has occurred!");
            return true;
        }
        String uuidInput = args[0];
        String cmdIdentifier = args[1];

        if (!uuidInput.equals(player.getUniqueId().toString())) return true;
        BookInterface.Button thisButton = buttons.get(cmdIdentifier);
        if (thisButton == null) return true;
        thisButton.runnable().run();

        return true;
    }

    public static void addButton(String identifier, BookInterface.Button button) {
        buttons.put(identifier, button);
        if (buttons.size() > 500) {
            String warning =
                """
                        ERROR ///////////////////////////////////////////////////////////////////////////////////////////////
                        ERROR //                                                                                           //
                        ERROR //                                          ______                                           //
                        ERROR //                                         //    \\\\                                          //
                        ERROR //                                        //      \\\\                                         //
                        ERROR //                                       //   ___  \\\\                                        //
                        ERROR //                                      //   |   |  \\\\                                       //
                        ERROR //                                     //    |   |   \\\\                                      //
                        ERROR //                                    //     |   |    \\\\                                     //
                        ERROR //                                   //      |   |     \\\\                                    //
                        ERROR //                                  //       |___|      \\\\                                   //
                        ERROR //                                 //         ___        \\\\                                  //
                        ERROR //                                //         |   |        \\\\                                 //
                        ERROR //                               //          |___|         \\\\                                //
                        ERROR //                              //                          \\\\                               //
                        ERROR //                             ////////////////////////////////                              //
                        ERROR //                                                                                           //
                        ERROR //       BUTTONS HAVE EXCEEDED 500, THIS IS LIKELY DUE TO MASS BookInterface CREATION        //
                        ERROR //                                                                                           //
                        ERROR ///////////////////////////////////////////////////////////////////////////////////////////////
                    """;
            UserInterface.getInstance().getLogger().severe(warning);
        }
    }

    public static void removeButton(String identifier) {
        buttons.remove(identifier);
    }

    public static boolean hasButtonIdentifier(String identifier) {
        return buttons.containsKey(identifier);
    }
}

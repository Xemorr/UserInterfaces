package me.xemor.userinterface.textinterface;

import org.bukkit.entity.Player;
import org.geysermc.cumulus.CustomForm;
import org.geysermc.floodgate.api.FloodgateApi;

import java.util.function.Consumer;

public class FloodgateGetInputTextInterface {

    public static boolean hackSoItDoesntCrash(Player player, TextInterface textInterface, Consumer<String> response) {
        if (FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId())) {
            CustomForm form = CustomForm.builder()
                    .title(textInterface.getTitle())
                    .input(textInterface.getInputName(), textInterface.getPlaceholder())
                    .responseHandler((string) -> response.accept(string.substring(2, string.length() - 3)))
                    .build();
            FloodgateApi.getInstance().sendForm(player.getUniqueId(), form);
            return true;
        }
        return false;
    }

}

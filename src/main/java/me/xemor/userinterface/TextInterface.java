package me.xemor.userinterface;

import me.xemor.userinterface.SignMenuFactory.SignMenuFactory;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.CustomForm;
import org.geysermc.floodgate.api.FloodgateApi;

import java.util.Collections;
import java.util.function.Consumer;

public class TextInterface {

    private String title;
    private String placeholder;
    private SignMenuFactory factory = new SignMenuFactory(UserInterface.getInstance());

    public void title(String title) {
        this.title = title;
    }

    public void placeholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public void getInput(Player player, Consumer<String> response) {
        if (FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId())) {
            CustomForm form = CustomForm.builder().title(title).input("Input", placeholder).responseHandler(response).build();
            FloodgateApi.getInstance().sendForm(player.getUniqueId(), form);
        }
        else {
            SignMenuFactory.Menu menu = factory.newMenu(Collections.singletonList(title));
            menu.reopenIfFail(true).response((ignored, output) -> {response.accept(output[1]); return true;});
        }
    }

}

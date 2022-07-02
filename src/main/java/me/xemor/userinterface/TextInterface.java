package me.xemor.userinterface;

import me.xemor.userinterface.SignMenuFactory.SignMenuFactory;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.CustomForm;
import org.geysermc.floodgate.api.FloodgateApi;

import java.util.Arrays;
import java.util.function.Consumer;

public class TextInterface {

    private String title;
    private String placeholder = "";
    private final SignMenuFactory factory = new SignMenuFactory(UserInterface.getInstance());

    public TextInterface title(String title) {
        this.title = title;
        return this;
    }

    public TextInterface placeholder(String placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    public void getInput(Player player, Consumer<String> response) {
        if (title == null) throw new IllegalStateException("Title is null! You must set title to use this class");
        if (FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId())) {
            CustomForm form = CustomForm.builder().title(title).input("Input", placeholder).responseHandler((string) -> response.accept(string.substring(2, string.length() - 3))).build();
            FloodgateApi.getInstance().sendForm(player.getUniqueId(), form);
        }
        else {
            SignMenuFactory.Menu menu = factory.newMenu(Arrays.asList("", "^^^^^^^^^^^", title, ""));
            menu.reopenIfFail(true).response((ignored, output) -> {response.accept(output[0]); return true;});
            menu.open(player);
        }
    }

}

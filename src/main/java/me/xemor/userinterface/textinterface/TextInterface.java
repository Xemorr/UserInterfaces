package me.xemor.userinterface.textinterface;

import me.xemor.userinterface.SignMenuFactory.SignMenuFactory;
import me.xemor.userinterface.UserInterface;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class TextInterface {

    private String title = "";
    private String placeholder = "";
    private String inputName = "Input";

    private final SignMenuFactory factory = new SignMenuFactory(UserInterface.getPlugin());

    public TextInterface() {
        if (!UserInterface.getPacketHandler().hasHook()) {
            throw new IllegalStateException("packetevents or ProtocolLib is required to use this feature");
        }
    }

    public TextInterface title(String title) {
        this.title = title;
        return this;
    }

    public TextInterface placeholder(String placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    public TextInterface inputName(String inputName) {
        this.inputName = inputName;
        return this;
    }

    protected String getTitle() {
        return title;
    }

    protected String getPlaceholder() {
        return placeholder;
    }

    protected String getInputName() {
        return inputName;
    }

    public void getInput(Player player, Consumer<String> response) {
        if (title == null) throw new IllegalStateException("Title is null! You must set a title to use this class");
        if (UserInterface.hasFloodgate() && FloodgateGetInputTextInterface.hackSoItDoesntCrash(player, this, response)) {}
        else {
            SignMenuFactory.Menu menu = factory.newMenu(Arrays.asList("", "^^^^^^^^^^^", title, ""));
            menu.reopenIfFail(true).response((ignored, output) -> {response.accept(output[0]); return true;});
            menu.open(player);
        }
    }

}

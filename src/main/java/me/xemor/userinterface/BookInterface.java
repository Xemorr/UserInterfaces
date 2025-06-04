package me.xemor.userinterface;

import me.xemor.userinterface.commands.BookInterfaceCmd;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.cumulus.util.FormImage;
import org.geysermc.floodgate.api.FloodgateApi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")
public class BookInterface {

    private String formTitle = "";
    private String content = "";
    private String bullet = "";
    private final List<Button> buttons = new ArrayList<>();

    private SimpleForm bedrockForm = null;

    public BookInterface title(String title) {
        this.formTitle = title;
        return this;
    }

    public BookInterface content(String content) {
        this.content = content;
        return this;
    }

    public BookInterface bullet(String bullet) {
        this.bullet = bullet;
        return this;
    }

    public BookInterface button(String buttonName, Runnable runnable) { return button(buttonName, runnable, "", ""); }

    public BookInterface button(String buttonName, Runnable runnable, String hoverText) { return button(buttonName, runnable, hoverText, ""); }

    public BookInterface button(String buttonName, Runnable runnable, String hoverText, String iconURL) {
        String identifier = createRandomIdentifier();
        Button button = new Button(identifier, runnable, buttonName, hoverText, iconURL);
        buttons.add(button);
        BookInterfaceCmd.addButton(identifier, button);
        return this;
    }

    public void open(Player player, Audience audience) {
        if (UserInterface.hasFloodgate() && FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId())) {
            if (bedrockForm == null) updateBedrockForm();
            FloodgateApi.getInstance().sendForm(player.getUniqueId(), bedrockForm);
        }
        else {
            Book book = createJavaBook(player);
            audience.openBook(book);
            player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1f, 1f);
        }
    }

    private Book createJavaBook(Player player) {
        TextComponent.Builder page = Component.text()
            .append(Component.text(content + "\n"));
        for (Button button : buttons) page.append(Component.text(button.name.replaceAll("%bullet%", bullet) + "\n").clickEvent(ClickEvent.runCommand("/ufbook " + UserInterface.getPlugin().getName().toLowerCase() + " "  + player.getUniqueId() + " " + button.identifier)).hoverEvent(Component.text(button.hoverText)));
        Collection<Component> pages = new ArrayList<>();
        pages.add(page.build());
        return Book.book(Component.text("UFBook"), Component.text("Dav_e_"), pages);
    }

    public void updateBedrockForm() {
        SimpleForm.Builder formBuilder = SimpleForm.builder()
            .title(formTitle)
            .content(content);
        for (Button button : buttons) formBuilder.button(button.name, FormImage.Type.URL, button.iconURL);

        formBuilder.responseHandler((form, responseData) -> {
            SimpleFormResponse response = form.parseResponse(responseData);
            if (!response.isCorrect()) return;
            buttons.get(response.getClickedButtonId()).runnable().run();
        });

        bedrockForm = formBuilder.build();
    }

    public void unload() { for (Button button : buttons) BookInterfaceCmd.removeButton(button.identifier); }

    public record Button(String identifier, Runnable runnable, String name, String hoverText, String iconURL) {}

    private String createRandomIdentifier() {
        String randomUUID = UUID.randomUUID().toString();
        if (BookInterfaceCmd.hasButtonIdentifier(randomUUID)) return createRandomIdentifier();
        return randomUUID;
    }
}

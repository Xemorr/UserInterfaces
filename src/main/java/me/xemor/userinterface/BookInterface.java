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

public class BookInterface {

    private String formTitle = "";
    private String content = "";
    private String bullet = "";
    private final List<Button> buttons = new ArrayList<>();

    private SimpleForm bedrockForm = null;

    public BookInterface title(String formTitle) {
        this.formTitle = formTitle;
        return this;
    }

    public BookInterface content(String title) {
        this.content = content;
        return this;
    }

    public BookInterface bullet(String bullet) {
        this.bullet = bullet;
        return this;
    }

    public void button(String buttonName, Runnable runnable) { button(buttonName, runnable, ""); }

    public void button(String buttonName, Runnable runnable, String iconURL) {
        String identifier = createRandomIdentifier();
        Button button = new Button(identifier, runnable, buttonName, iconURL);
        buttons.add(button);
        BookInterfaceCmd.addButton(identifier, button);
    }

    public void open(Player player) {
        if (UserInterface.hasFloodgate() && FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId())) {
            if (bedrockForm == null) updateBedrockForm();
            FloodgateApi.getInstance().sendForm(player.getUniqueId(), bedrockForm);
        }
        else {
            Book book = createJavaBook(player);
            Audience audience = UserInterface.getBukkitAudiences().sender(player);
            audience.openBook(book);
            player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1f, 1f);
        }
    }

    private Book createJavaBook(Player player) {
        TextComponent.Builder page = Component.text().append(Component.text(content + "\n\n"));
        for (Button button : buttons) page.append(Component.text(button.name.replaceAll("%bullet%", bullet) + "\n").clickEvent(ClickEvent.runCommand("/ufbook " + player.getUniqueId() + " " + button.identifier)));
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

    public record Button(String identifier, Runnable runnable, String name, String iconURL) {}

    private String createRandomIdentifier() {
        String randomUUID = UUID.randomUUID().toString();
        if (BookInterfaceCmd.hasButtonIdentifier(randomUUID)) return createRandomIdentifier();
        return randomUUID;
    }
}

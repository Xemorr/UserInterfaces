package me.xemor.userinterface.chestinterface;

import me.xemor.userinterface.UserInterface;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

@SuppressWarnings("unused")
public class InventoryInteractions<T> implements InventoryHolder {

    @Nullable
    private Consumer<Player> closeInteraction;
    private final Map<Predicate<ItemStack>, Interaction> map = new HashMap<>();
    private Inventory inventory;
    private boolean isClosed;
    private final T data;

    public InventoryInteractions(T data) {
        this.data = data;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        if (inventory == null) throw new IllegalArgumentException("InventoryInterface has not been constructed correctly. You have to pass the inventory using" +
                "InventoryInterface#setInventory");
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public void addCloseInteraction(Consumer<Player> consumer) {
        closeInteraction = consumer;
    }

    public void closed(Player player) {
        isClosed = true;
        if (closeInteraction != null) {
            closeInteraction.accept(player);
        }
    }

    public void addItemInteraction(Predicate<ItemStack> itemStackPredicate, Interaction interaction) {
        map.put(itemStackPredicate, interaction);
    }

    public void addItemSimpleInteraction(ItemStack item, Consumer<Player> consumer) {
        map.put(item::equals,
                (player, otherItem, clickType) ->  {
                    if (clickType == ClickType.LEFT || clickType == ClickType.RIGHT) consumer.accept(player);
                }
        );
    }

    public void addSlotInteraction(Predicate<Character> slotPredicate, Interaction interaction) {
        map.put((item) -> slotPredicate.test(item.getItemMeta().getPersistentDataContainer().get(UserInterface.getSlotNameKey(), PersistentDataType.STRING).charAt(0)), interaction);
    }

    public void addSlotSimpleInteraction(char slot, Consumer<Player> consumer) {
        map.put((item) -> slot == item.getItemMeta().getPersistentDataContainer().get(UserInterface.getSlotNameKey(), PersistentDataType.STRING).charAt(0),
                (player, otherItem, clickType) ->  {
                    if (clickType == ClickType.LEFT || clickType == ClickType.RIGHT) consumer.accept(player);
                }
        );
    }

    public void interact(ItemStack item, Player player, ClickType clickType) {
        for (Map.Entry<Predicate<ItemStack>, Interaction> entry : map.entrySet()) {
            if (entry.getKey().test(item)) {
                entry.getValue().run(player, item, clickType);
            }
        }
    }

    public T getData() {
        return data;
    }

    public interface Interaction {
        void run(Player player, ItemStack clicked, ClickType clickType);
    }

    public boolean isClosed() {
        return isClosed;
    }
}

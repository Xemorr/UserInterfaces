package me.xemor.userinterface.chestinterface;


import me.xemor.userinterface.UserInterface;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Takes in an interface string such as
 * """
 * +       -
 *     3
 * /       _
 * """
 */
@SuppressWarnings("unused")
public class ChestInterface<T> {

    private final Inventory inventory;

    public ChestInterface(String name, int rows, T data) {
        InventoryInteractions<T> inventoryInterface = new InventoryInteractions<>(data);
        inventory = Bukkit.createInventory(inventoryInterface, rows * 9, name);
        inventoryInterface.setInventory(inventory);
    }

    public void calculateInventoryContents(String[] layout, Map<Character, ItemStack> key) {
        for (int i = 0; i < layout.length * 9; i++) {
            char character = layout[i / 9].charAt(i % 9);
            ItemStack itemStack = key.getOrDefault(character, new ItemStack(Material.AIR));
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.getPersistentDataContainer().set(UserInterface.getSlotNameKey(), PersistentDataType.STRING, character + "");
            inventory.setItem(i, itemStack);
        }
    }

    public Inventory getInventory() {
        return inventory;
    }

    public InventoryInteractions<T> getInteractions() {
        return (InventoryInteractions<T>) inventory.getHolder();
    }

    public static ChestInterface<ChestInterfaceBehaviour> createFromChestInterfaceFactory(ChestInterfaceConfig config, ChestInterfaceBehaviour factory) {
        Class<? extends ChestInterfaceBehaviour> clazz = factory.getClass();
        List<Method> interactions = Arrays.stream(clazz.getMethods()).filter(it -> it.isAnnotationPresent(InteractionHandler.class)).toList();
        ChestInterface<ChestInterfaceBehaviour> chestInterface = new ChestInterface<>(config.name(), config.layout().length, factory);
        interactions.forEach(it -> {
            Parameter[] parameters = it.getParameters();
            if (parameters.length == 1) {
                Class<?> param1 = parameters[0].getType();
                chestInterface.getInteractions().addSlotSimpleInteraction(
                        it.getAnnotation(InteractionHandler.class).slot(),
                        (player) -> {
                            try {
                                it.invoke(factory, player);
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                throw new RuntimeException(e);
                            }
                        }
                );
            }
            else if (parameters.length == 3) {
                Class<?> param1 = parameters[0].getType();
                Class<?> param2 = parameters[1].getType();
                Class<?> param3 = parameters[2].getType();

                chestInterface.getInteractions().addSlotInteraction(
                        (character) -> character == it.getAnnotation(InteractionHandler.class).slot(),
                        (player, clicked, clickType) -> {
                            try {
                                it.invoke(factory, player, clicked, clickType);
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                throw new RuntimeException(e);
                            }
                        }
                );
            }
        });
        chestInterface.calculateInventoryContents(config.layout(), config.buttons());
        return chestInterface;
    }

}

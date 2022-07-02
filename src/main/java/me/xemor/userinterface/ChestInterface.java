package me.xemor.userinterface;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * Takes in an interface string such as
 * """
 * +       -
 *     3
 * /       _
 * """
 */
public class ChestInterface<T> {

    private final Inventory inventory;

    public ChestInterface(String name, String[] layout, Map<Character, ItemStack> key, T data) {
        InventoryInteractions<T> inventoryInterface = new InventoryInteractions<>(data);
        inventory = Bukkit.createInventory(inventoryInterface, layout.length * 9, name);
        recalculateInventoryContents(layout, key, inventoryInterface);
    }

    public void recalculateInventoryContents(String[] layout, Map<Character, ItemStack> key, InventoryInteractions<T> inventoryInterface) {
        inventoryInterface.setInventory(inventory);
        for (int i = 0; i < layout.length * 9; i++) {
            char character = layout[i / 9].charAt(i % 9);
            ItemStack itemStack = key.getOrDefault(character, new ItemStack(Material.AIR));
            inventory.setItem(i, itemStack);
        }
    }

    public Inventory getInventory() {
        return inventory;
    }

    public InventoryInteractions<T> getInteractions() {
        return (InventoryInteractions<T>) inventory.getHolder();
    }

}

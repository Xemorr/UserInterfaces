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

    public ChestInterface(String name, int rows, T data) {
        InventoryInteractions<T> inventoryInterface = new InventoryInteractions<>(data);
        inventory = Bukkit.createInventory(inventoryInterface, rows * 9, name);
        inventoryInterface.setInventory(inventory);
    }

    public void calculateInventoryContents(String[] layout, Map<Character, ItemStack> key) {
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

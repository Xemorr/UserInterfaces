package me.xemor.userinterface;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public class ChestHandler implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        ItemStack clickedItem = e.getCurrentItem();
        Inventory inventory = e.getInventory();
        if (inventory.getHolder() instanceof InventoryInteractions) {
            InventoryInteractions<?> inventoryInterface = (InventoryInteractions<?>) inventory.getHolder();
            if (e.getWhoClicked() instanceof Player) {
                inventoryInterface.interact(clickedItem, (Player) e.getWhoClicked(), e.getClick());
            }
            e.setCancelled(true);
        }
    }

}

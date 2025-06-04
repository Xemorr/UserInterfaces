package me.xemor.userinterface;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public class ChestHandler implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        ItemStack clickedItem = e.getCurrentItem();
        Inventory inventory = e.getInventory();
        if (e.getClickedInventory() == null) return;
        if (e.getClickedInventory().getHolder() instanceof InventoryInteractions<?> inventoryInterface) {
            e.setCancelled(true);
            if (e.getWhoClicked() instanceof Player) {
                inventoryInterface.interact(clickedItem, (Player) e.getWhoClicked(), e.getClick());
            }
        } else if (e.getWhoClicked().getOpenInventory().getTopInventory().getHolder() instanceof InventoryInteractions) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        Inventory inventory = e.getInventory();
        if (inventory.getHolder() instanceof InventoryInteractions<?> inventoryInteractions) {
            if (!inventoryInteractions.isClosed()) {
                if (e.getPlayer() instanceof Player) {
                    inventoryInteractions.closed((Player) e.getPlayer());
                }
            }
        }
    }

}

package me.xemor.userinterface.chestinterface;

import org.bukkit.inventory.ItemStack;

import java.util.Map;

public record ChestInterfaceConfig(String name, String[] layout, Map<Character, ItemStack> key) {}

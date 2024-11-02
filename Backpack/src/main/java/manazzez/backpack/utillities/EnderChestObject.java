package manazzez.backpack.utillities;

import manazzez.backpack.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EnderChestObject {
    private Inventory inventory;
    private String ownerName;
    private Player owner;
    private int size;

    // Constructor for new EnderChestObject with a player
    public EnderChestObject(Player player) {
        this.owner = player;
        this.ownerName = player.getName().toLowerCase();
        this.size = 27; // Default size, can be adjusted
        createInventory(this.size, player.getName());
    }

    // Constructor for loading an EnderChestObject
    public EnderChestObject(String ownerName, int size, ItemStack[] inventoryContents) {
        this.ownerName = ownerName.toLowerCase();
        this.size = size;
        createInventory(size, ownerName);
        setInventoryContents(inventoryContents);
    }

    // Create inventory with given size
    private void createInventory(int size, String displayName) {
        this.inventory = Bukkit.createInventory(null, size, ChatColor.WHITE + "Enderchest - " + capitalizeFirstLetter(displayName));
    }

    // Set inventory contents
    private void setInventoryContents(ItemStack[] inventoryContents) {
        for (int i = 0; i < inventoryContents.length && i < this.size; i++) {
            this.inventory.setItem(i, inventoryContents[i]);
        }
    }

    // Method to set the player when they log in
    public void setPlayer(Player player) {
        this.owner = player;
    }

    // Method to resize the inventory
    public void resizeInventory(int newSize) {
        this.size = newSize;
        createInventory(newSize, ownerName);
        ItemStack[] currentItems = this.inventory.getContents();
        setInventoryContents(currentItems);
    }

    // Method to open the inventory
    public void open(Player player) {
        player.openInventory(getInventory());
    }

    // Getters and setters
    public ItemStack[] getItems() {
        return inventory.getContents();
    }

    public int getSize() {
        return size;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public Inventory getInventory() {
        return inventory;
    }

    private String capitalizeFirstLetter(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

}

package manazzez.backpack.utillities;

import manazzez.backpack.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class BackpackObject {

    private PublicState publicState;
    private int number;
    private int slot;
    private short code;
    private boolean isExpanded;
    private boolean isPublic;
    private String backpackName;
    private String ownerName;
    private Player owner;
    private Material iconMaterial;
    private List<String> lore;
    private Inventory inventory;
    private ArrayList<String> whoCanAccess;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isExpanded() {
        return isExpanded;
    }
    public boolean isPublic() {
        return isPublic;
    }
    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public String getBackpackName() {
        return backpackName;
    }

    public void setBackpackName(String backpackName) {
        if(!backpackName.contains("&")) {
            backpackName = ChatColor.WHITE + backpackName;
        }
        backpackName = backpackName.replace("\\u0026", "&");
        backpackName = backpackName.replace("\\u0027", "'");
        this.backpackName = ChatColor.translateAlternateColorCodes('&', backpackName);
        updateInventoryName();
    }
    private void updateInventoryName() {
        int size = inventory.getSize();
        ItemStack[] contents = inventory.getContents();
        this.inventory = Bukkit.createInventory(owner, size, this.backpackName);
        this.inventory.setContents(contents);
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Material getIconMaterial() {
        return iconMaterial;
    }

    public short getCode() {
        return code;
    }

    public void setCode(short code) {
        this.code = code;
    }
    public ArrayList<String> getWhoCanAccess() {
        return whoCanAccess;
    }
    public void setWhoCanAccess(ArrayList<String> whoCanAccess) {
        this.whoCanAccess = whoCanAccess;
    }

    public void setIconMaterial(Material iconMaterial) {
        this.iconMaterial = iconMaterial;
    }

    public List<String> getLore() {
        return lore;
    }

    public void setLore(List<String> lore) {
        lore.replaceAll(s -> s.replace("\\u0026", "&"));
        lore.replaceAll(s -> s.replace("\\u0027", "'"));
        lore.replaceAll(s -> ChatColor.translateAlternateColorCodes('&', s));
        this.lore = lore;
    }

    public String getOwnerName() {
        return ownerName;
    }
    public PublicState getPublicState() {
        return publicState;
    }

    public void setPublicState(PublicState publicState) {
        this.publicState = publicState;
    }
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public ItemStack[] getItems() {
        return inventory.getContents();
    }

    public int getSlot() {
        return slot;
    }

    public int setSize() {
        if (isExpanded) {
            return 54;
        } else {
            return 27;
        }
    }


    private void updateInventorySize() {
        int newSize = setSize();
        Inventory newInventory = Bukkit.createInventory(owner, newSize, ChatColor.BOLD + getBackpackName());
        ItemStack[] currentItems = inventory.getContents();
        for (int i = 0; i < currentItems.length && i < newSize; i++) {
            newInventory.setItem(i, currentItems[i]);
        }
        this.inventory = newInventory;
    }

    public void open(Player player) {
        player.openInventory(getInventory());
    }

    public boolean setExpanded(boolean expanded) {
        this.isExpanded = expanded;
        updateInventorySize();
        return this.isExpanded;
    }

    public ItemStack getIconItemStack() {
        ItemStack itemStack = new ItemStack(getIconMaterial(), getNumber(), getCode());
        ItemMeta meta = itemStack.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);
        meta.setDisplayName(this.backpackName);
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public BackpackObject(Player player, int number) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        this.owner = player;
        this.ownerName = player.getName().toLowerCase();
        this.iconMaterial = Material.CHEST;
        this.publicState = PublicState.PRIVATE;
        this.number = number; // Explicit number
        this.whoCanAccess = new ArrayList<>();
        this.backpackName = ChatColor.translateAlternateColorCodes('&', "&fBackpack " + number);

        // Calculate the slot
        if (Main.getBackpacksMap().get(ownerName).size() > 6) {
            this.slot = number + 11;
        } else {
            this.slot = number + 9;
        }

        this.isExpanded = false;
        this.inventory = Bukkit.createInventory(player, setSize(), getBackpackName());
        this.lore = Arrays.asList(ChatColor.GRAY + "Left click to open the backpack", ChatColor.GRAY + "Right click to edit the backpack");
    }

    // Data-loading constructor
    public BackpackObject(String ownerName, String backpackName, Material iconMaterial, boolean isExpanded, short code, List<String> lore, ItemStack[] items, int slot, int number) {
        this.ownerName = ownerName.toLowerCase();
        this.backpackName = backpackName;
        this.iconMaterial = iconMaterial;
        this.isExpanded = isExpanded;
        this.code = code;
        this.lore = lore;
        this.slot = slot;
        this.number = number;
        this.inventory = Bukkit.createInventory(null, items.length, ChatColor.BOLD + backpackName);
        this.inventory.setContents(items);
    }



}

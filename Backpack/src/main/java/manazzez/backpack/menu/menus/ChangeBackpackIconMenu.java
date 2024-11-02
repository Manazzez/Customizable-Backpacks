package manazzez.backpack.menu.menus;

import manazzez.backpack.Main;
import manazzez.backpack.menu.Menu;
import manazzez.backpack.menu.PlayerMenuUtility;
import manazzez.backpack.utillities.BackpackObject;
import manazzez.backpack.utillities.ItemStacks;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

public class ChangeBackpackIconMenu extends Menu {

    public ChangeBackpackIconMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public String getMenuName() {
        return ChatColor.WHITE + "Change backpack icon";
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        e.setCancelled(true);
        if (e.getClick().isShiftClick() || e.getClick().isKeyboardClick() || e.getCurrentItem() == null || e.getCurrentItem().equals(new ItemStack(Material.AIR))) {
            return;
        }

        if (e.getCurrentItem().equals(ItemStacks.customIcon())) {
            if (e.getCursor() != null && e.getCursor().getType() != Material.AIR) {
                for (BackpackObject backpack : Main.getBackpacksMap().get(p.getName().toLowerCase())) {
                    if (backpack.getNumber() == BackpacksMenu.clickedBackpack.get(p)) {
                        ItemStack itemInHand = e.getCursor();
                        ItemStack clone = itemInHand.clone();

                        p.playSound(p.getLocation(), Sound.ITEM_PICKUP, 1, 1);

                        backpack.setIconMaterial(clone.getType());
                        backpack.setCode(clone.getDurability());

                        p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&7You've changed the icon of your backpack &b" + backpack.getNumber() + "&7 to: &b" + clone.getType()));
                        p.closeInventory();
                        BackpacksMenu backpacksMenu = new BackpacksMenu(Main.getPlayerMenuUtility(p));
                        backpacksMenu.open();
                        return;
                    }
                }
            } else {
                if (e.isRightClick()) {
                    p.closeInventory();
                    BackpacksMenu backpacksMenu = new BackpacksMenu(Main.getPlayerMenuUtility(p));
                    backpacksMenu.open();
                    return;
                }
                p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
            }
        } else {
            for (ItemStack inventoryItens : e.getInventory().getContents()) {
                for (BackpackObject backpacks : Main.getBackpacksMap().get(p.getName().toLowerCase())) {
                    if (backpacks.getNumber() == BackpacksMenu.clickedBackpack.get(p)) {
                        if (e.getCurrentItem().equals(inventoryItens) && e.getSlot() != 49) {
                            BackpackEditMenu editBackpackMenu = new BackpackEditMenu(Main.getPlayerMenuUtility(p));
                            backpacks.setIconMaterial(inventoryItens.getType());
                            backpacks.setCode(inventoryItens.getDurability());
                            p.closeInventory();
                            editBackpackMenu.open();
                            p.playSound(p.getLocation(), Sound.ITEM_PICKUP, 1, 1);
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                                        "&7You've changed the icon of your backpack &b" + backpacks.getNumber() + "&7 to: &b" + inventoryItens.getType()));
                            return;
                        }
                    }
                }
            }
        }
        if(e.getSlot() == 49 && !e.getCurrentItem().equals(ItemStacks.customIcon())) {
            for (BackpackObject backpacks : Main.getBackpacksMap().get(p.getName().toLowerCase())) {
                if (backpacks.getNumber() == BackpacksMenu.clickedBackpack.get(p) && e.isRightClick() && e.getCurrentItem() != ItemStacks.customIcon()) {
                    if (e.getClick().isRightClick()) {
                        backpacks.setIconMaterial(Material.CHEST);
                        backpacks.setCode((short) 0);
                        p.playSound(p.getLocation(), Sound.GLASS, 1, 1);
                        p.closeInventory();
                        open();
                    }
                    if (e.getClick().isLeftClick()) {
                        p.closeInventory();
                        BackpackEditMenu editBackpackMenu = new BackpackEditMenu(Main.getPlayerMenuUtility(p));
                        editBackpackMenu.open();
                    }
                }
            }
        }

    }

    @Override
    public void setMenuItems() {
        for (BackpackObject backpacks : Main.getBackpacksMap().get(playerMenuUtility.getOwner().getName().toLowerCase())) {
            if (backpacks.getNumber() == BackpacksMenu.clickedBackpack.get(playerMenuUtility.getOwner())) {
                if (backpacks.getIconMaterial().equals(Material.CHEST)) {
                    inventory.setItem(49, ItemStacks.customIcon());
                } else {
                    ItemStack customIcon = backpacks.getIconItemStack();
                    customIcon.setAmount(1);
                    ItemMeta meta = customIcon.getItemMeta();
                    meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Custom Icon");
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    meta.setLore(Arrays.asList(ChatColor.GRAY + "If you want to change the icon again right click this",
                            ChatColor.GRAY + "If you want to return to the edit menu left click this "));
                    customIcon.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
                    customIcon.setItemMeta(meta);

                    inventory.setItem(49, customIcon);
                }
            }

        }
        inventory.setItem(10, new ItemStack(Material.REDSTONE_TORCH_ON));
        inventory.setItem(11, new ItemStack(Material.REDSTONE));
        inventory.setItem(12, new ItemStack(Material.IRON_INGOT));
        inventory.setItem(13, new ItemStack(Material.GOLD_INGOT));
        inventory.setItem(14, new ItemStack(Material.EMERALD));
        inventory.setItem(15, new ItemStack(Material.DIAMOND));
        inventory.setItem(16, new ItemStack(Material.COAL));
        inventory.setItem(19, new ItemStack(Material.FURNACE));
        inventory.setItem(20, new ItemStack(Material.STONE));
        inventory.setItem(21, new ItemStack(Material.COBBLESTONE));
        inventory.setItem(22, new ItemStack(Material.WOOD));
        inventory.setItem(23, new ItemStack(Material.LOG));
        inventory.setItem(24, new ItemStack(Material.WORKBENCH));
        inventory.setItem(25, new ItemStack(Material.WOOD_AXE));
        inventory.setItem(28, new ItemStack(Material.OBSIDIAN));
        inventory.setItem(29, new ItemStack(Material.BEDROCK));
        inventory.setItem(30, new ItemStack(Material.ENCHANTMENT_TABLE));
        inventory.setItem(30, new ItemStack(Material.ENCHANTED_BOOK));
        inventory.setItem(31, new ItemStack(Material.BOOK_AND_QUILL));
        inventory.setItem(32, new ItemStack(Material.COMPASS));
        inventory.setItem(33, new ItemStack(Material.TNT));
        inventory.setItem(34, new ItemStack(Material.BED));
        inventory.setItem(37, new ItemStack(Material.DIAMOND_HELMET));
        inventory.setItem(38, new ItemStack(Material.DIAMOND_CHESTPLATE));
        inventory.setItem(39, new ItemStack(Material.DIAMOND_LEGGINGS));
        inventory.setItem(40, new ItemStack(Material.DIAMOND_BOOTS));
        inventory.setItem(43, new ItemStack(Material.GOLDEN_APPLE, 1, (short) 1));
        inventory.setItem(41, new ItemStack(Material.DIAMOND_SWORD));
        inventory.setItem(42, new ItemStack(Material.POTION, 1, (short) 5));

    }
}

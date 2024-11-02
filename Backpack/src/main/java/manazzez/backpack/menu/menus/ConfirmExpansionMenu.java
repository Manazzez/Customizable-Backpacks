package manazzez.backpack.menu.menus;

import manazzez.backpack.Config;
import manazzez.backpack.Main;
import manazzez.backpack.menu.Menu;
import manazzez.backpack.menu.PlayerMenuUtility;
import manazzez.backpack.utillities.BackpackObject;
import manazzez.backpack.utillities.ItemStacks;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ConfirmExpansionMenu extends Menu {

    int expansion = Config.getSettingsConfig().getInt("Settings.ExpansionPrice");

    public ConfirmExpansionMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public String getMenuName() {
        return ChatColor.WHITE + "Confirm backpack expansion";
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&fExpansion price: &e" + expansion));
        Player player = (Player) e.getWhoClicked();
        e.setCancelled(true);
        if (e.getCurrentItem().equals(ItemStacks.redWool())) {
            player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1, 1);
            player.closeInventory();
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&7You've&c declined&7 the purchase of the backpack expansion"));
            BackpacksMenu backpacksMenu = new BackpacksMenu(Main.getPlayerMenuUtility(player));
            backpacksMenu.open();
        } else if (e.getCurrentItem().equals(ItemStacks.grayWool())) {
            player.playSound(player.getLocation(), Sound.ITEM_BREAK, 1, 1);
        } else if (e.getCurrentItem().equals(ItemStacks.greenWool())) {
            if (Main.getFinances().get(player) > expansion - 1 && Main.getBackpacksMap().containsKey(playerMenuUtility.getOwner().getName().toLowerCase())) {
                  for (BackpackObject backpackObject : Main.getBackpacksMap().get(playerMenuUtility.getOwner().getName().toLowerCase())) {
                        if (backpackObject.getNumber() == BackpacksMenu.clickedBackpack.get(playerMenuUtility.getOwner())) {
                            Main.getFinances().put(player, Main.getFinances().get(player) - expansion);
                            player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1, 1);
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                                        "&7Your backpack got successfully &eexpanded&7 to 54 slots"));
                            player.closeInventory();
                            backpackObject.setExpanded(true);
                            BackpacksMenu backpacksMenu = new BackpacksMenu(Main.getPlayerMenuUtility(player));
                            backpacksMenu.open();
                            BackpacksMenu.clickedBackpack.remove(player);
                    }
                }
            }
        }
        
    }

    @Override
    public void setMenuItems() {
        inventory.setItem(13, ItemStacks.cart());
        inventory.setItem(15, ItemStacks.redWool());
        if (Main.getFinances().get(playerMenuUtility.getOwner()) < expansion - 1) {
            inventory.setItem(11, ItemStacks.grayWool());
        } else {
            inventory.setItem(11, ItemStacks.greenWool());
        }
    }
}

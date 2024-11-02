package manazzez.backpack.menu.menus;

import manazzez.backpack.Config;
import manazzez.backpack.Main;
import manazzez.backpack.menu.Menu;
import manazzez.backpack.menu.PlayerMenuUtility;
import manazzez.backpack.utillities.BackpackManager;
import manazzez.backpack.utillities.BackpackObject;
import manazzez.backpack.utillities.ItemStacks;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConfirmPurchaseMenu extends Menu {

    int value = Config.getSettingsConfig().getInt("Settings.BackpackPrice");
    int expansion = Config.getSettingsConfig().getInt("Settings.ExpansionPrice");

    public ConfirmPurchaseMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public String getMenuName() {
        return ChatColor.WHITE + "Finish purchasing your backpack";
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        e.setCancelled(true);
        if(e.getCurrentItem().equals(ItemStacks.redWool())) {
            player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1 , 1);
            player.closeInventory();
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                        "&7You've&c declined&7 the purchase of the backpack"));
            BackpacksMenu backpacksMenu = new BackpacksMenu(Main.getPlayerMenuUtility(player));
            backpacksMenu.open();
        } else if (e.getCurrentItem().equals(ItemStacks.grayWool()))  {
            player.playSound(player.getLocation(), Sound.ITEM_BREAK, 1, 1);
        } else if(e.getCurrentItem().equals(ItemStacks.greenWool())) {
            if(Main.getFinances().get(player) > value - 1) {
                Main.getFinances().put(player, Main.getFinances().get(player) - value);
                player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1, 1);
                player.closeInventory();
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You've&a acquired&7 a new backpack!"));

                String playerName = player.getName().toLowerCase();
                Map<String, Integer> backpackCounters = BackpackManager.getBackpackCounters();
                int number = backpackCounters.getOrDefault(playerName, 0) + 1; // Assign a unique number
                backpackCounters.put(playerName, number);

                BackpackObject backpackObject = new BackpackObject(player, number); // Use the constructor with number
                HashMap<String, ArrayList<BackpackObject>> backpacksMap = Main.getBackpacksMap();
                ArrayList<BackpackObject> backpackList = backpacksMap.get(playerName);
                if (backpackList == null) {
                    backpackList = new ArrayList<>();
                }
                if(BackpacksSettingsMenu.getAutomaticBackpackExpansion().contains(player.getName().toLowerCase())) {
                    if(Main.getFinances().get(player) > expansion - 1) {
                        Main.getFinances().put(player, Main.getFinances().get(player) - expansion);
                        backpackObject.setExpanded(true);
                    } else {
                        player.sendMessage(ChatColor.GRAY + "Couldn't expand backpack automatically because you don't have enough coins");
                        backpackObject.setExpanded(false);
                    }
                }
                backpackList.add(backpackObject);
                backpacksMap.put(playerName, backpackList);

                BackpacksMenu backpacksMenu = new BackpacksMenu(Main.getPlayerMenuUtility(player));
                backpacksMenu.open();
                BackpacksMenu.clickedBackpack.remove(player);
            }
        } else if(e.getCurrentItem().equals(ItemStacks.chest())) {
            player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1, 1);
        }

    }

    @Override
    public void setMenuItems() {
        inventory.setItem(15, ItemStacks.redWool());
        inventory.setItem(13, ItemStacks.chest());
        if(Main.getFinances().get(playerMenuUtility.getOwner()) < value) {
            inventory.setItem(11, ItemStacks.grayWool());
        } else {
            inventory.setItem(11, ItemStacks.greenWool());
        }
    }
}

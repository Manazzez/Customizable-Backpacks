package manazzez.backpack.menu.menus;

import lombok.Getter;
import manazzez.backpack.Config;
import manazzez.backpack.Main;
import manazzez.backpack.menu.Menu;
import manazzez.backpack.menu.PlayerMenuUtility;
import manazzez.backpack.utillities.ItemStacks;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;

public class BackpacksSettingsMenu extends Menu {

    @Getter
    private static final ArrayList<String> returnToBackpackMenu = new ArrayList<>();
    @Getter
    private static final ArrayList<String> automaticBackpackExpansion = new ArrayList<>();

    public BackpacksSettingsMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public int getSlots() {
        return 36;
    }

    @Override
    public String getMenuName() {
        return ChatColor.WHITE + "Backpacks Settings";
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        FileConfiguration config = Config.getContentsConfig();
        Player player = (Player) e.getWhoClicked();
        e.setCancelled(true);

        if (e.getCurrentItem().equals(ItemStacks.returnArrow())) {
            player.closeInventory();
            player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1, 1);
            BackpacksMenu backpacksMenu = new BackpacksMenu(Main.getPlayerMenuUtility(player));
            backpacksMenu.open();
        } else if (e.getCurrentItem().equals(ItemStacks.toggle(false)) && e.getSlot() == 19) {
            returnToBackpackMenu.add(player.getName().toLowerCase());
            config.set(player.getName().toLowerCase() + ".settings.returnToBackpackMenu", true);
            Config.saveContentsConfig();
            player.closeInventory();
            open();
        } else if (e.getCurrentItem().equals(ItemStacks.toggle(true)) && e.getSlot() == 19) {
            returnToBackpackMenu.remove(player.getName().toLowerCase());
            config.set(player.getName().toLowerCase() + ".settings.returnToBackpackMenu", false);
            Config.saveContentsConfig();
            player.closeInventory();
            open();
        } else if (e.getCurrentItem().equals(ItemStacks.toggle(false)) && e.getSlot() == 21) {
            automaticBackpackExpansion.add(player.getName().toLowerCase());
            config.set(player.getName().toLowerCase() + ".settings.automaticBackpackExpansion", true);
            Config.saveContentsConfig();
            player.closeInventory();
            open();
        } else if (e.getCurrentItem().equals(ItemStacks.toggle(true)) && e.getSlot() == 21) {
            automaticBackpackExpansion.remove(player.getName().toLowerCase());
            config.set(player.getName().toLowerCase() + ".settings.automaticBackpackExpansion", false);
            Config.saveContentsConfig();
            player.closeInventory();
            open();
        }
    }

    @Override
    public void setMenuItems() {
        inventory.setItem(12 , ItemStacks.autoExpandBackpacks());
        inventory.setItem(31, ItemStacks.returnArrow());
        if(returnToBackpackMenu.contains(playerMenuUtility.getOwner().getName().toLowerCase())) {
            inventory.setItem(19, ItemStacks.toggle(true));
            inventory.setItem(10, ItemStacks.lightOn());
        } else {
            inventory.setItem(19, ItemStacks.toggle(false));
            inventory.setItem(10, ItemStacks.lightOff());
        }
        if(automaticBackpackExpansion.contains(playerMenuUtility.getOwner().getName().toLowerCase())) {
            inventory.setItem(21, ItemStacks.toggle(true));
        } else {
            inventory.setItem(21, ItemStacks.toggle(false));
        }
    }

}

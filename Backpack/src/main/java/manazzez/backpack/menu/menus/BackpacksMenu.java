package manazzez.backpack.menu.menus;

import manazzez.backpack.Main;
import manazzez.backpack.menu.Menu;
import manazzez.backpack.menu.PlayerMenuUtility;
import manazzez.backpack.utillities.BackpackObject;
import manazzez.backpack.utillities.EnderChestObject;
import manazzez.backpack.utillities.ItemStacks;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class BackpacksMenu extends Menu {

    public BackpacksMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    public static HashMap<Player, Integer> clickedBackpack = new HashMap<>();

    @Override
    public int getSlots() {
        return 36;
    }

    @Override
    public String getMenuName() {
        return ChatColor.WHITE + "Backpacks Menu";
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        e.setCancelled(true);
        if (e.isShiftClick() || e.getClick().equals(ClickType.SHIFT_LEFT) || e.getClick().equals(ClickType.SHIFT_RIGHT) || e.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) {
            e.setCancelled(true);
            return;
        }
        if (e.getCursor().hasItemMeta()) {
            return;
        }
        if (e.getCurrentItem().equals(ItemStacks.pane())) {
            ConfirmPurchaseMenu confirmPurchaseMenu = new ConfirmPurchaseMenu(Main.getPlayerMenuUtility(player));
            confirmPurchaseMenu.open();
        }
        if (e.getCurrentItem().equals(ItemStacks.gear())) {
            if (Main.getBackpacksMap().containsKey(player.getName().toLowerCase())) {
                BackpacksSettingsMenu backpacksSettingsMenu = new BackpacksSettingsMenu(Main.getPlayerMenuUtility(player));
                backpacksSettingsMenu.open();
            } else {
                player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1, 1);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&7You need at least 1 backpack to access this menu"));
            }
        }
        if (e.getCurrentItem().equals(ItemStacks.enderChest())) {
            if(e.getClick().equals(ClickType.LEFT) || e.getClick().equals(ClickType.SHIFT_LEFT)) {
                if (!Main.getEnderchestMap().containsKey(player.getName().toLowerCase())) {
                    EnderChestObject enderChestObject = new EnderChestObject(player);
                    enderChestObject.open(player);
                    Main.getEnderchestMap().put(player.getName().toLowerCase(), enderChestObject);
                } else {
                    EnderChestObject enderChestObject = Main.getEnderchestMap().get(player.getName().toLowerCase());
                    enderChestObject.open(player);
                }
            }
            if(e.getClick().equals(ClickType.RIGHT) || e.getClick().equals(ClickType.SHIFT_RIGHT)) {
                EChestEditMenu eChestEditMenu = new EChestEditMenu(Main.getPlayerMenuUtility(player));
                eChestEditMenu.open();
            }
        }
        if (Main.getBackpacksMap().containsKey(player.getName().toLowerCase())) {
            for (BackpackObject backpackObject : Main.getBackpacksMap().get(player.getName().toLowerCase())) {
                if (backpackObject.getOwner().getName().equalsIgnoreCase(player.getName().toLowerCase())) {
                    if (e.getCurrentItem().equals(backpackObject.getIconItemStack())) {
                        if (e.isLeftClick()) {
                            backpackObject.open(player);
                        } else {
                            clickedBackpack.remove(player);
                            clickedBackpack.put(player, backpackObject.getNumber());
                            BackpackEditMenu backpackEditMenu = new BackpackEditMenu(Main.getPlayerMenuUtility(player));
                            backpackEditMenu.open();
                        }
                    }
                }
            }
        }

    }

    @Override
    public void setMenuItems() {
        inventory.setItem(30, ItemStacks.question());
        inventory.setItem(31, ItemStacks.gear());
        inventory.setItem(32, ItemStacks.enderChest());
        int[] panes = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25};
        for (int i : panes) {
            inventory.setItem(i, ItemStacks.pane());
        }
        if (Main.getBackpacksMap().containsKey(playerMenuUtility.getOwner().getName().toLowerCase())) {
            for (BackpackObject backpackObject : Main.getBackpacksMap().get(playerMenuUtility.getOwner().getName().toLowerCase())) {
                if (backpackObject.getOwner().getName().equalsIgnoreCase(playerMenuUtility.getOwner().getName().toLowerCase())) {
                    inventory.setItem(backpackObject.getSlot(), backpackObject.getIconItemStack());
                }
            }
        }

    }
}

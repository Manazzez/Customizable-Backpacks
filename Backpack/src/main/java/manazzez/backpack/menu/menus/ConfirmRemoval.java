package manazzez.backpack.menu.menus;

import manazzez.backpack.Main;
import manazzez.backpack.menu.Menu;
import manazzez.backpack.menu.PlayerMenuUtility;
import manazzez.backpack.utillities.BackpackObject;
import manazzez.backpack.utillities.ItemStacks;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class ConfirmRemoval extends Menu {

    public ConfirmRemoval(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public String getMenuName() {
        return ChatColor.WHITE + "Confirm everyone's removal";
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        e.setCancelled(true);
        Player player = (Player) e.getWhoClicked();
        if(e.getCurrentItem().equals(decline())) {
            PickedPeopleMenu pickedPeopleMenu = new PickedPeopleMenu(Main.getPlayerMenuUtility(player));
            pickedPeopleMenu.open();
            player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1, 1);
        } else if(e.getCurrentItem().equals(accept())) {
            for(BackpackObject backpackObject : Main.getBackpacksMap().get(player.getName().toLowerCase())) {
                if(backpackObject.getNumber() == BackpacksMenu.clickedBackpack.get(player)) {
                    backpackObject.getWhoCanAccess().clear();
                    PickedPeopleMenu pickedPeopleMenu = new PickedPeopleMenu(Main.getPlayerMenuUtility(player));
                    pickedPeopleMenu.open();
                    player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1, 1);
                }
            }
        }
    }

    @Override
    public void setMenuItems() {
        inventory.setItem(11, accept());
        inventory.setItem(13, explanation());
        inventory.setItem(15, decline());
    }
    public static ItemStack decline() {
        ItemStack item = ItemStacks.redWool();
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Decline");
        meta.setLore(Arrays.asList(
                ChatColor.GRAY + "Decline the removal of all the players",
                ChatColor.GRAY + "who have access to this backpack."
        ));
        item.setItemMeta(meta);
        return item;
    }
    public static ItemStack accept() {
        ItemStack item = ItemStacks.greenWool();
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Accept");
        meta.setLore(Arrays.asList(
                ChatColor.GRAY + "Accept the removal of all the players",
                ChatColor.GRAY + "who have access to this backpack."

        ));
        item.setItemMeta(meta);
        return item;
    }
    public static ItemStack explanation() {
        ItemStack item = new ItemStack(Material.HOPPER_MINECART);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Remove all the players");
        meta.setLore(Arrays.asList(
                ChatColor.GRAY + "By clicking the green wool you will",
                ChatColor.GRAY + "remove everyone's access to this backpack!"
        ));
        item.setItemMeta(meta);

        return item;
    }


}

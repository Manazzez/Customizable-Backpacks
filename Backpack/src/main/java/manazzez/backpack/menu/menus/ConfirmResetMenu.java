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
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ConfirmResetMenu extends Menu {

    public ConfirmResetMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }
    int countdownTime = 5;

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public String getMenuName() {
        return ChatColor.WHITE + "Confirm backpack reset";
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        e.setCancelled(true);
        if (e.getCurrentItem().equals(decline())) {
            player.closeInventory();
            BackpacksMenu.clickedBackpack.remove(player);
            player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1, 1);
            BackpacksMenu backpacksMenu = new BackpacksMenu(Main.getPlayerMenuUtility(player));
            backpacksMenu.open();
        } else if(e.getCurrentItem().equals(accept())) {
            for(BackpackObject backpack : Main.getBackpacksMap().get(player.getName().toLowerCase())) {
                if(backpack.getNumber() == BackpacksMenu.clickedBackpack.get(player)) {
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1, 1);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Your backpack &b" + backpack.getNumber() + "&7 has successfully been reset"));
                    backpack.setBackpackName(ChatColor.WHITE + "Backpack " + backpack.getNumber());
                    backpack.setIconMaterial(Material.CHEST);
                    backpack.setOwner(player);
                    backpack.setLore(Arrays.asList(ChatColor.GRAY + "Left click to open the backpack", ChatColor.GRAY + "Right click to edit the backpack"));
                    BackpacksMenu.clickedBackpack.remove(player);
                }
            }
        } else if(e.getCurrentItem().equals(wait(countdownTime))) {
            player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1, 1);
        }
    }

    @Override
    public void setMenuItems() {
        inventory.setItem(13, ItemStacks.warning());
        inventory.setItem(15, decline());
        startCountdown(playerMenuUtility.getOwner());

    }

    private void startCountdown(Player player) {
        new BukkitRunnable() {
            int count = countdownTime;
            @Override
            public void run() {
                if (count > 0) {
                    inventory.setItem(11, ConfirmResetMenu.this.wait(count));
                    player.updateInventory();
                    count--;
                } else {
                    inventory.setItem(11, accept());
                    player.updateInventory();
                    cancel();
                }
            }
        }.runTaskTimer(Main.getInstance(), 0L, 20L); // Run task every second (20 ticks)
    }

    public ItemStack decline() {
        ItemStack item = ItemStacks.redWool();
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Decline");
        meta.setLore(Collections.singletonList(ChatColor.GRAY + "If you changed your mind just click this"));
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack accept() {
        ItemStack item = ItemStacks.greenWool();
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Reset");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "By clicking here you'll reset all the", ChatColor.GRAY + "customization you've done to this backpack"));
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack wait(int seconds) {
        ItemStack item = ItemStacks.grayWool();
        item.setAmount(seconds);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_GRAY + "Wait " + seconds + " seconds");
        meta.setLore(new ArrayList<>());
        item.setItemMeta(meta);
        return item;
    }
}

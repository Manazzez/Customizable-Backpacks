package manazzez.backpack.menu.menus;

import lombok.Getter;
import manazzez.backpack.Commands;
import manazzez.backpack.Main;
import manazzez.backpack.menu.Menu;
import manazzez.backpack.menu.PlayerMenuUtility;
import manazzez.backpack.utillities.BackpackObject;
import manazzez.backpack.utillities.EnderChestObject;
import manazzez.backpack.utillities.ItemStacks;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AdminViewerMenu extends Menu {

    private static final ArrayList<String> InteractWithItens = new ArrayList<>();
    private static final ArrayList<String> adminInUse = new ArrayList<>();
    private static final ArrayList<String> openInventory = new ArrayList<>();

    private static final HashMap<Player,String> targetMap = Commands.getTargetMap();
    private static final HashMap<Player,String> map = new HashMap<>(Commands.getTargetMap());

    public AdminViewerMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public int getSlots() {
        return 36;
    }

    @Override
    public String getMenuName() {
        return ChatColor.WHITE + capitalizeFirstLetter(Commands.getTargetMap().get(playerMenuUtility.getOwner())) + "'s Backpacks";

    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        e.setCancelled(true);
        Player player = (Player) e.getWhoClicked();
        if (e.getCurrentItem().equals(ItemStacks.interactOff())) {
            InteractWithItens.add(player.getName().toLowerCase());
            inventory.setItem(30, ItemStacks.interactOn());
            player.updateInventory();
        } else if (e.getCurrentItem().equals(ItemStacks.interactOn())) {
            InteractWithItens.remove(player.getName().toLowerCase());
            inventory.setItem(30, ItemStacks.interactOff());
            player.updateInventory();
        } else if(e.getCurrentItem().equals(ItemStacks.noEnderchest())) {
            player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1, 1);

        }
//      updateCopyMap();
        if (e.getCurrentItem().equals(targetEnderChest())) {
            if (e.getClick().equals(ClickType.LEFT)) {
                EnderChestObject enderChestObject = Main.getEnderchestMap().get(Commands.getTargetMap().get(player));
                openInventory.removeIf(player.getName().toLowerCase()::equals);
                openInventory.add(player.getName().toLowerCase());
                enderChestObject.open(player);
            }
        }
        for (BackpackObject targetBackpacks : Main.getBackpacksMap().get(Commands.getTargetMap().get(player))) {
            if (targetBackpacks.getOwnerName().equalsIgnoreCase(Commands.getTargetMap().get(player))) {
                if (e.getCurrentItem().equals(targetBackpacks.getIconItemStack())) {
                    openInventory.removeIf(player.getName().toLowerCase()::equals);
                    openInventory.add(player.getName().toLowerCase());
                    targetBackpacks.open(player);
                }
            }
        }
    }

    @Override
    public void setMenuItems() {
        int[] panes = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25};
        for (int i : panes) {
            inventory.setItem(i, ItemStacks.pane());
        }
        if(Main.getEnderchestMap().containsKey(Commands.getTargetMap().get(playerMenuUtility.getOwner()))) {
            inventory.setItem(31, targetEnderChest());
        } else {
            inventory.setItem(31, ItemStacks.noEnderchest());
        }
        if (InteractWithItens.contains(playerMenuUtility.getOwner().getName().toLowerCase())) {
            inventory.setItem(30, ItemStacks.interactOn());
        } else {
            inventory.setItem(30, ItemStacks.interactOff());
        }
        for (BackpackObject targetBackpacks : Main.getBackpacksMap().get(Commands.getTargetMap().get(playerMenuUtility.getOwner()))) {
            if (targetBackpacks.getOwnerName().equalsIgnoreCase(Commands.getTargetMap().get(playerMenuUtility.getOwner()))) {
                inventory.setItem(targetBackpacks.getSlot(), targetBackpacks.getIconItemStack());
            }
        }

    }

    public static ArrayList<String> getInteractWithItens() {
        return InteractWithItens;
    }
    public static ArrayList<String> getOpenInventory() {return openInventory;}
    public static ArrayList<String> getAdminInUse() {return adminInUse;}

    private ItemStack targetEnderChest() {
        String string = Commands.getTargetMap().get(playerMenuUtility.getOwner());
        ItemStack item = ItemStacks.enderChest();
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + capitalizeFirstLetter(string) + "'s Enderchest");
        meta.setLore(Collections.singletonList(ChatColor.GRAY + "Acess " + capitalizeFirstLetter(Commands.getTargetMap().get(playerMenuUtility.getOwner())) + "'s enderchest"));
        item.setItemMeta(meta);
        return item;
    }
    public Inventory getInventory() {
        return this.inventory;
    }

    public static String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }
    private static void updateCopyMap() {
        for (Map.Entry<Player, String> entry : targetMap.entrySet()) {
            if (entry.getValue() != null) {
                map.put(entry.getKey(), entry.getValue());
            }
        }

        // Debug print to verify the updates
        map.forEach((player, value) -> {
            System.out.println("Player: " + player.getName() + ", Value: " + value);
        });
    }
}

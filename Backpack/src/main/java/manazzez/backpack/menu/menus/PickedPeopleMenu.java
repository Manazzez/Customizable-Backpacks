package manazzez.backpack.menu.menus;

import manazzez.backpack.Config;
import manazzez.backpack.Main;
import manazzez.backpack.menu.PaginatedMenu;
import manazzez.backpack.menu.PlayerMenuUtility;
import manazzez.backpack.menu.signs.SignGUIAPI;
import manazzez.backpack.utillities.BackpackObject;
import manazzez.backpack.utillities.ItemStacks;
import manazzez.backpack.utillities.SkullCreator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;

public class PickedPeopleMenu extends PaginatedMenu {

    private BackpackObject backpackObject = null;


    public static ArrayList<String> whoCanAccess;
    public static ArrayList<ItemStack> whoCanAccessHeads = new ArrayList<>();

    public PickedPeopleMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public String getMenuName() {
        return ChatColor.WHITE + "Add or remove players";
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        e.setCancelled(true);
        Player player = (Player) e.getWhoClicked();
        ItemStack clickedItem = e.getCurrentItem();

        if (clickedItem.equals(ItemStacks.nextPage(false)) || clickedItem.equals(ItemStacks.previousPage(false))) {
            player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1, 1);
            return;
        } else if (clickedItem.equals(ItemStacks.nextPage(true))) {
            page = page + 1;
            open();
            return;
        } else if (clickedItem.equals(ItemStacks.previousPage(true))) {
            page = page - 1;
            open();
            return;
        }

        String skullName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());

        if (clickedItem.equals(ItemStacks.addPlayerToHandPickedList())) {
            player.closeInventory();
            player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1, 1);
            addPlayerToChest(player);
        } else if (e.getCurrentItem().equals(ItemStacks.removeEveryone(whoCanAccessHeads))) {
            ConfirmRemoval confirmRemoval = new ConfirmRemoval(Main.getPlayerMenuUtility(player));
            confirmRemoval.open();
        } else if (clickedItem.equals(ItemStacks.returnArrow())) {
            BackpackPublicnessMenu backpackPublicnessMenu = new BackpackPublicnessMenu(Main.getPlayerMenuUtility(player));
            backpackPublicnessMenu.open();
        } else if (clickedItem.getType().equals(Material.SKULL_ITEM)
                && !skullName.equalsIgnoreCase("Next page") && !skullName.equalsIgnoreCase("Previous page")) {
            if (e.getClick().isRightClick()) {
                SkullMeta meta = (SkullMeta) clickedItem.getItemMeta();
                String headOwner = ChatColor.stripColor(meta.getDisplayName().toLowerCase());

                boolean removed = whoCanAccess.removeIf(item -> item.equalsIgnoreCase(headOwner));

                if (removed) {
                    backpackObject.getWhoCanAccess().remove(headOwner);
                    backpackObject.getWhoCanAccess().remove(headOwner);
                    whoCanAccess.remove(headOwner);
                    setMenuItems(); // Refresh the inventory
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&7You've removed " + AdminViewerMenu.capitalizeFirstLetter(headOwner) + "'s &7access"));
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
                } else {
                    player.sendMessage(ChatColor.RED + "That player doesn't have access.");
                    player.playSound(player.getLocation(), Sound.NOTE_BASS, 1, 1);
                }
            }
        }
    }

    @Override
    public void setMenuItems() {
        whoCanAccessHeads.clear();
        inventory.clear();
        index = 0;

        for (BackpackObject backpackObject : Main.getBackpacksMap().get(playerMenuUtility.getOwner().getName().toLowerCase())) {
            if (backpackObject.getNumber() == BackpacksMenu.clickedBackpack.get(playerMenuUtility.getOwner())) {
                this.backpackObject = backpackObject;
                whoCanAccess = new ArrayList<>(backpackObject.getWhoCanAccess());
                for (String name : whoCanAccess) {
                    whoCanAccessHeads.add(heads(name.toLowerCase()));
                }
                addMenuBorders(whoCanAccessHeads.size());
                break;
            }
        }

        if (whoCanAccessHeads != null && !whoCanAccessHeads.isEmpty()) {
            for (int i = 0; i < super.maxItemsPerPage; i++) {
                index = super.maxItemsPerPage * page + i;
                if (index >= whoCanAccessHeads.size()) break;
                if (whoCanAccessHeads.get(index) != null) {
                    inventory.addItem(whoCanAccessHeads.get(index));
                }
            }
        }
        inventory.setItem(51, ItemStacks.removeEveryone(whoCanAccessHeads));
        inventory.setItem(47, ItemStacks.addPlayerToHandPickedList());
    }

    public static ItemStack heads(String owner) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwner(owner);
        meta.setDisplayName(ChatColor.AQUA + AdminViewerMenu.capitalizeFirstLetter(owner));
        meta.setLore(Arrays.asList(
                ChatColor.translateAlternateColorCodes('&', "&7The player &b" + AdminViewerMenu.capitalizeFirstLetter(owner) + "&7 has access to this backpack."),
                ChatColor.GRAY + "To remove his access just right click this item"));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack explanation() {
        String string = "http://textures.minecraft.net/texture/4b7f663d65cded7bd3651bddd6db546360dd773abbdaf48b83aee08e1cbe14";
        ItemStack item = SkullCreator.itemFromUrl(string);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "Help");
        meta.setLore(Arrays.asList(
                "",
                ""
        ));
        item.setItemMeta(meta);
        return item;
    }

    public void addPlayerToChest(Player player) {
        SignGUIAPI.builder()
                .action(event -> {
                    String playerName = event.getLines().get(0).toLowerCase();

                    if (playerName.isEmpty()) {
                        openHandPickedMenu(player);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Please enter a valid player name."));
                        return;
                    }

//                    if (!Main.getBackpacksMap().containsKey(playerName) || !Config.getContentsConfig().contains(playerName)) {
//                        openHandPickedMenu(player);
//                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7The player &c" + AdminViewerMenu.capitalizeFirstLetter(playerName) + "&7 has not played here before."));
//                        return;
//                    }
//
//                    if (playerName.equalsIgnoreCase(player.getName())) {
//                        openHandPickedMenu(player);
//                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You can't add your&c own&7 name to this list."));
//                        return;
//                    }
//
//                    if (backpackObject.getWhoCanAccess().contains(playerName)) {
//                        player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1, 1);
//                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7The player &c" + AdminViewerMenu.capitalizeFirstLetter(playerName) + "&7 already has access to this backpack"));
//                        return;
//                    }

                    backpackObject.getWhoCanAccess().add(playerName);
                    PickedPeopleMenu pickedPeopleMenu = new PickedPeopleMenu(Main.getPlayerMenuUtility(player));
                    pickedPeopleMenu.open();
                    player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1, 1);

                }).withLines(Arrays.asList("",
                        ChatColor.translateAlternateColorCodes('&', "&fUse the line atop"),
                        ChatColor.translateAlternateColorCodes('&', "&fto name who you"),
                        ChatColor.translateAlternateColorCodes('&', "&fwant to whitelist")
                ))
                .plugin(Main.getInstance())
                .name(player.getName())
                .build()
                .open();
    }

    private void openHandPickedMenu(Player player) {
        PickedPeopleMenu pickedPeopleMenu = new PickedPeopleMenu(Main.getPlayerMenuUtility(player));
        pickedPeopleMenu.open();
        player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1, 1);
    }

}

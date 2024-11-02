package manazzez.backpack.menu.menus;


import manazzez.backpack.Main;
import manazzez.backpack.menu.Menu;
import manazzez.backpack.menu.PlayerMenuUtility;
import manazzez.backpack.menu.signs.SignGUIAPI;
import manazzez.backpack.utillities.BackpackObject;
import manazzez.backpack.utillities.ItemStacks;
import manazzez.backpack.utillities.PublicState;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.Arrays;

public class BackpackEditMenu extends Menu {

    public BackpackEditMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public String getMenuName() {
        if (Main.getBackpacksMap().containsKey(playerMenuUtility.getOwner().getName().toLowerCase())) {
            for (BackpackObject backpackObject : Main.getBackpacksMap().get(playerMenuUtility.getOwner().getName().toLowerCase())) {
                if (backpackObject.getNumber() == BackpacksMenu.clickedBackpack.get(playerMenuUtility.getOwner())) {
                    return ChatColor.WHITE + "Edit backpack " + backpackObject.getNumber();
                }
            }
        }
        return ChatColor.WHITE + "Edit backpack ";
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        e.setCancelled(true);
        Player player = (Player) e.getWhoClicked();
        if (e.getCurrentItem().equals(ItemStacks.returnArrow())) {
            BackpacksMenu backpacksMenu = new BackpacksMenu(Main.getPlayerMenuUtility(player));
            BackpacksMenu.clickedBackpack.remove(player);
            player.closeInventory();
            player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1, 1);
            backpacksMenu.open();
        } else if (e.getCurrentItem().equals(ItemStacks.changeIcon(player))) {
            ChangeBackpackIconMenu changeBackpackIconMenu = new ChangeBackpackIconMenu(Main.getPlayerMenuUtility(player));
            player.closeInventory();
            player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1, 1);
            changeBackpackIconMenu.open();
        } else if (e.getCurrentItem().equals(ItemStacks.resetBackpack())) {
            ConfirmResetMenu confirmResetMenu = new ConfirmResetMenu(Main.getPlayerMenuUtility(player));
            player.closeInventory();
            player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1, 1);
            confirmResetMenu.open();
        } else if (e.getCurrentItem().equals(ItemStacks.notExpandedBackpack())) {
            ConfirmExpansionMenu confirmExpansionMenu = new ConfirmExpansionMenu(Main.getPlayerMenuUtility(player));
            player.closeInventory();
            player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1, 1);
            confirmExpansionMenu.open();
        } else if (e.getCurrentItem().equals(ItemStacks.expandedBackpack())) {
            player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1, 1);
        } else if (e.getCurrentItem().equals(ItemStacks.renameBackpack())) {
            renameBackpack(player);
        } else if (e.getCurrentItem().equals(ItemStacks.defineLore())) {
            setLore(player);
        }
        if (e.getCurrentItem().getType().equals(Material.SKULL_ITEM)) {
            for (BackpackObject backpackObject : Main.getBackpacksMap().get(playerMenuUtility.getOwner().getName().toLowerCase())) {
                if (backpackObject.getNumber() == BackpacksMenu.clickedBackpack.get(playerMenuUtility.getOwner())) {
                    if (e.getCurrentItem().equals(ItemStacks.backpackAccessibility(backpackObject))) {
                        BackpackPublicnessMenu publicnessMenu = new BackpackPublicnessMenu(Main.getPlayerMenuUtility(player));
                        publicnessMenu.open();
                    } else if (e.getCurrentItem().equals(ItemStacks.backpackAccessibility(backpackObject))) {
                        BackpackPublicnessMenu publicnessMenu = new BackpackPublicnessMenu(Main.getPlayerMenuUtility(player));
                        publicnessMenu.open();
                    }
                }
            }
        }
    }

    @Override
    public void setMenuItems() {
        inventory.setItem(16, ItemStacks.returnArrow());
        inventory.setItem(10, ItemStacks.changeIcon(playerMenuUtility.getOwner()));
        inventory.setItem(12, ItemStacks.renameBackpack());
        inventory.setItem(13, ItemStacks.defineLore());
        inventory.setItem(14, ItemStacks.resetBackpack());

        if (Main.getBackpacksMap().containsKey(playerMenuUtility.getOwner().getName().toLowerCase())) {
            for (BackpackObject backpackObject : Main.getBackpacksMap().get(playerMenuUtility.getOwner().getName().toLowerCase())) {
                if (backpackObject.getNumber() == BackpacksMenu.clickedBackpack.get(playerMenuUtility.getOwner())) {
                    if (!backpackObject.isExpanded()) {
                        inventory.setItem(11, ItemStacks.notExpandedBackpack());
                    } else {
                        inventory.setItem(11, ItemStacks.expandedBackpack());
                    }
                    inventory.setItem(15, ItemStacks.backpackAccessibility(backpackObject));

                }
            }
        }
    }

    public void renameBackpack(Player player) {
        for (BackpackObject backpackObject : Main.getBackpacksMap().get(player.getName().toLowerCase())) {
            if (backpackObject.getNumber() == BackpacksMenu.clickedBackpack.get(player)) {
                SignGUIAPI.builder()
                        .action(event -> {
                            if (event.getLines().get(0).isEmpty()) {
                                event.getPlayer().sendMessage(ChatColor.GRAY + "You can't name a backpack blank");
                                event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.VILLAGER_NO, 1, 1);
                                warningRename(event.getPlayer());
                            } else {
                                BackpackEditMenu backpackEditMenu = new BackpackEditMenu(Main.getPlayerMenuUtility(player));
                                backpackEditMenu.open();
                                backpackObject.setBackpackName(event.getLines().get(0));
                                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',
                                        "&7You've changed the name of your backpack&b " + backpackObject.getNumber() + "&7 to: " + backpackObject.getBackpackName()));
                            }
                        })
                        .withLines(Arrays.asList(ChatColor.translateAlternateColorCodes('&', ""), "&fUse the line above", "&fto define the name", "&fof the backpack"))
                        .plugin(Main.getInstance())
                        .name(player.getName())
                        .build()
                        .open();

            }
        }
    }

    public void warningRename(Player player) {
        BackpacksMenu backpacksMenu = new BackpacksMenu(Main.getPlayerMenuUtility(player));
        for (BackpackObject backpackObject : Main.getBackpacksMap().get(playerMenuUtility.getOwner().getName().toLowerCase())) {
            if (backpackObject.getNumber() == BackpacksMenu.clickedBackpack.get(playerMenuUtility.getOwner())) {
                SignGUIAPI.builder()
                        .action(event -> {
                            if (event.getLines().get(0).isEmpty()) {
                                event.getPlayer().sendMessage(ChatColor.GRAY + "You can't name a backpack blank");
                                event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.VILLAGER_NO, 1, 1);
                                player.closeInventory();
                                backpacksMenu.open();
                            } else {
                                BackpackEditMenu backpackEditMenu = new BackpackEditMenu(Main.getPlayerMenuUtility(player));
                                backpackEditMenu.open();
                                backpackObject.setBackpackName(event.getLines().get(0));
                                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',
                                        "&7You've changed the name of your backpack&b " + backpackObject.getNumber() + "&7 to: " + backpackObject.getBackpackName()));
                            }
                        })
                        .withLines(Arrays.asList("", "&cYou can't pass", "&cAn empty name.", "&cPress ESC to stop"))
                        .plugin(Main.getInstance())
                        .name(player.getName())
                        .build()
                        .open();
            }
        }
    }

    public void setLore(Player player) {
        for (BackpackObject backpackObject : Main.getBackpacksMap().get(player.getName().toLowerCase())) {
            if (backpackObject.getNumber() == BackpacksMenu.clickedBackpack.get(player)) {
                SignGUIAPI.builder()
                        .action(event -> {
                            ArrayList<String> lore = new ArrayList<>();
                            for (String line : event.getLines()) {
                                if (!line.trim().isEmpty()) {
                                    lore.add(ChatColor.GRAY + line);
                                }
                            }
                            BackpackEditMenu backpackEditMenu = new BackpackEditMenu(Main.getPlayerMenuUtility(player));
                            backpackEditMenu.open();
                            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ITEM_PICKUP, 1, 1);
                            event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes(
                                    '&', "&7You've changed the &blore&7 on your backpack &b" + backpackObject.getNumber()));
                            backpackObject.setLore(lore);
                        })
                        .withLines(Arrays.asList("", "", "", ""))
                        .plugin(Main.getInstance())
                        .name(player.getName())
                        .build()
                        .open();
            }
        }
    }
}

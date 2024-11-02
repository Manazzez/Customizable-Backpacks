package manazzez.backpack.utillities;

import manazzez.backpack.Commands;
import manazzez.backpack.Config;
import manazzez.backpack.Main;
import manazzez.backpack.menu.Menu;
import manazzez.backpack.menu.menus.AdminViewerMenu;
import manazzez.backpack.menu.menus.BackpacksMenu;
import manazzez.backpack.menu.menus.BackpacksSettingsMenu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Listeners implements Listener {

    private static final HashMap<String, ArrayList<BackpackObject>> backpackMap = Main.getBackpacksMap();
    private static final HashMap<String, EnderChestObject> enderchestMap = Main.getEnderchestMap();
    private static final HashMap<Player, String> targetMap = Commands.getTargetMap();
    private static final ArrayList<String> returnEnderchest = new ArrayList<>();

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        String playerName = player.getName().toLowerCase();
        if(!backpackMap.containsKey(player.getName().toLowerCase())) {
            if(!Config.getContentsConfig().contains(player.getName().toLowerCase())) {
                backpackMap.put(player.getName().toLowerCase(), new ArrayList<>());
            }
        }
        if (Main.getBackpacksMap().containsKey(playerName)) {
            for (BackpackObject backpack : Main.getBackpacksMap().get(playerName)) {
                backpack.setOwner(player);
            }
        }
        if (!Main.getEnderchestMap().containsKey(player.getName().toLowerCase())) {
            EnderChestObject enderChestObject = new EnderChestObject(player);
            enderChestObject.setOwner(player);
        }
        EnderChestObject enderChestObject = Main.getEnderchestMap().get(playerName);
        if (enderChestObject != null) {
            enderChestObject.setPlayer(player);
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player player = e.getPlayer();
    }

    @EventHandler
    public void onMenuClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        String pName = p.getName().toLowerCase();
        Inventory clickedInventory = e.getClickedInventory();

        if (clickedInventory == null) {
            return;
        }

        if (!AdminViewerMenu.getInteractWithItens().contains(p.getName().toLowerCase()) && AdminViewerMenu.getAdminInUse().contains(p.getName().toLowerCase())) {
            for (Map.Entry<String, ArrayList<BackpackObject>> entry : backpackMap.entrySet()) {
                String backpackOwnerName = entry.getKey();
                if (isBackpack(entry.getValue(), clickedInventory)) {
                    if (targetMap.containsKey(p) && targetMap.get(p).equals(backpackOwnerName)) {
                        e.setCancelled(true);
                    }
                }
            }
            for (Map.Entry<String, EnderChestObject> entry : enderchestMap.entrySet()) {
                String enderChestOwnerName = entry.getKey();
                if (isEnderChest(entry.getValue(), clickedInventory)) {
                    if (targetMap.containsKey(p) && targetMap.get(p).equals(enderChestOwnerName)) {
                        e.setCancelled(true);
                    }
                }

            }
        }
        InventoryHolder h = e.getClickedInventory().getHolder();
        if (h instanceof Menu) {
            if (e.getCurrentItem() == null) {
                return;
            }
            if (e.getClickedInventory() == null) {
                return;
            }
            if (e.getClickedInventory().getHolder() == null) {
                return;
            }
            if (e.getClick().isShiftClick() || e.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) {
                e.setCancelled(true);
            }

            Menu menu = (Menu) h;
            menu.handleMenu(e);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock().getType().equals(Material.ENDER_CHEST)) {
            e.setCancelled(true);
            if (!Main.getEnderchestMap().containsKey(player.getName().toLowerCase())) {
                EnderChestObject enderChestObject = new EnderChestObject(player);
                enderChestObject.open(player);
                Main.getEnderchestMap().put(player.getName().toLowerCase(), enderChestObject);
            } else {
                returnEnderchest.add(player.getName().toLowerCase());
                EnderChestObject enderChestObject = Main.getEnderchestMap().get(player.getName().toLowerCase());
                enderChestObject.open(player);
            }
        }
        if (e.getAction().equals(Action.LEFT_CLICK_BLOCK) && e.getClickedBlock().getType().equals(Material.ENDER_CHEST)) {

        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        BackpacksMenu backpacksMenu = new BackpacksMenu(Main.getPlayerMenuUtility(p));
        AdminViewerMenu adminViewerMenu = new AdminViewerMenu(Main.getPlayerMenuUtility(p));

        if (e.getInventory().getHolder() instanceof AdminViewerMenu && !AdminViewerMenu.getOpenInventory().contains(p.getName().toLowerCase())) {
            AdminViewerMenu.getAdminInUse().remove(p.getName().toLowerCase());
            AdminViewerMenu.getOpenInventory().removeIf(p.getName().toLowerCase()::equals);
            Commands.getTargetMap().remove(p);

        }
        if (!AdminViewerMenu.getAdminInUse().contains(p.getName().toLowerCase())) {
            if (BackpacksSettingsMenu.getReturnToBackpackMenu().contains(p.getName().toLowerCase()) && !Commands.getTargetMap().containsKey(p)) {
                if (Main.getBackpacksMap().containsKey(p.getName().toLowerCase())) {
                    for (BackpackObject backpack : Main.getBackpacksMap().get(p.getName().toLowerCase())) {
                        if (e.getInventory().equals(backpack.getInventory())) {
                            if (backpack.getOwner().equals(p)) {
                                Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
                                    @Override
                                    public void run() {
                                        backpacksMenu.open();
                                    }
                                }, 1);
                            }
                        }
                    }
                }
                if (Main.getEnderchestMap().containsKey(p.getName().toLowerCase())) {
                    EnderChestObject enderChestObject = Main.getEnderchestMap().get(p.getName().toLowerCase());
                    if (e.getInventory().equals(enderChestObject.getInventory())) {
                        if (enderChestObject.getOwner().equals(p)) {
                            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
                                @Override
                                public void run() {
                                    backpacksMenu.open();
                                }
                            }, 1);
                        }
                    }
                }
            }
        } else if (AdminViewerMenu.getAdminInUse().contains(p.getName().toLowerCase())) {
            if (Main.getBackpacksMap().containsKey(p.getName().toLowerCase())) {
                for (BackpackObject tBackpacks : Main.getBackpacksMap().get(Commands.getTargetMap().get(p))) {
                    if (tBackpacks.getOwnerName().equalsIgnoreCase(Commands.getTargetMap().get(p)) && e.getInventory().equals(tBackpacks.getInventory())) {
                        AdminViewerMenu.getOpenInventory().remove(p.getName().toLowerCase());
                        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
                            @Override
                            public void run() {
                                adminViewerMenu.open();
                            }
                        }, 1);
                    }
                }
            }
            if(Main.getEnderchestMap().containsKey(targetMap.get(p))) {
                EnderChestObject enderChestObject = Main.getEnderchestMap().get(Commands.getTargetMap().get(p));
                if (e.getInventory().equals(enderChestObject.getInventory())) {
                    AdminViewerMenu.getOpenInventory().remove(p.getName().toLowerCase());
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            adminViewerMenu.open();
                        }
                    }, 1);
                }
            }
        }
    }

    private boolean isBackpack(ArrayList<BackpackObject> backpacks, Inventory inventory) {
        for (BackpackObject backpackObject : backpacks) {
            if (backpackObject.getInventory().equals(inventory)) {
                return true;
            }
        }
        return false;
    }

    private boolean isEnderChest(EnderChestObject enderChestObject, Inventory inventory) {
        return enderChestObject.getInventory().equals(inventory);
    }
//    private void prinLists() {
//        ArrayList<ArrayList<String>> toPrint = new ArrayList<>();
//        toPrint.add(AdminViewerMenu.getAdminInUse());
//        toPrint.add(AdminViewerMenu.getInteractWithItens());
//        toPrint.add(AdminViewerMenu.getOpenInventory());
//
//        String[] listNames = {"AdminInUse", "InteractWithItems", "OpenInventory"};
//
//        for (int i = 0; i < toPrint.size(); i++) {
//            ArrayList<String> currentList = toPrint.get(i);
//            System.out.println("Items from " + listNames[i] + ":");
//            for (String item : currentList) {
//                System.out.println(" - " + item);
//            }
//        }
//    }
//    private void printMap() {
//        for(Map.Entry<Player, String> entry : targetMap.entrySet()) {
//            Player player = entry.getKey();
//            String target = entry.getValue();
//            System.out.println("Admin: " + player.getName() + "| Target: " + target);
//        }
//    }
}

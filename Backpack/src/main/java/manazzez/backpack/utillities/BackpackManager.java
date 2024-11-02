package manazzez.backpack.utillities;

import manazzez.backpack.Config;
import manazzez.backpack.Main;
import manazzez.backpack.menu.menus.BackpacksSettingsMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class BackpackManager {

    private static final HashMap<String, ArrayList<BackpackObject>> backpacksMap = Main.getBackpacksMap();
    private static final Map<String, Integer> backpackCounters = new HashMap<>();

    public static void saveBackpacks() {
        FileConfiguration config = Config.getContentsConfig();

        for (Map.Entry<String, ArrayList<BackpackObject>> entry : backpacksMap.entrySet()) {
            String playerName = entry.getKey();
            ArrayList<BackpackObject> backpacks = entry.getValue();

            for (BackpackObject backpack : backpacks) {
                String path = playerName + "." + backpack.getNumber();
                config.set(path + ".backpackName", backpack.getBackpackName());
                config.set(path + ".ownerName", backpack.getOwnerName());
                config.set(path + ".iconMaterial", backpack.getIconMaterial().name());
                config.set(path + ".isExpanded", backpack.isExpanded());
                config.set(path + ".code", backpack.getCode());
                config.set(path + ".lore", backpack.getLore());
                config.set(path + ".inventory", backpack.getItems());
                config.set(path + ".slot", backpack.getSlot());
                config.set(path + ".publicState", backpack.getPublicState().name());
                config.set(path + ".whoCanAccess", backpack.getWhoCanAccess());
            }
        }
        Config.saveContentsConfig();

    }

    public static void loadBackpacks() {
        FileConfiguration config = Config.getContentsConfig();

        if (config.getKeys(false).isEmpty()) {
            System.out.println("Config is empty. Nothing to load.");
            return;
        }

        for (String playerName : config.getKeys(false)) {
            ArrayList<BackpackObject> backpacks = new ArrayList<>();

            for (String numberStr : config.getConfigurationSection(playerName).getKeys(false)) {
                if (isNotNumber(numberStr)) continue;
                int number = Integer.parseInt(numberStr);
                String path = playerName + "." + number;
                String backpackName = config.getString(path + ".backpackName");
                String ownerName = config.getString(path + ".ownerName");
                Material iconMaterial = Material.valueOf(config.getString(path + ".iconMaterial"));
                boolean isExpanded = config.getBoolean(path + ".isExpanded");
                short code = (short) config.getInt(path + ".code");
                String publicStateStr = config.getString(path + ".publicState");
                PublicState publicState = publicStateStr != null ? PublicState.valueOf(publicStateStr) : PublicState.PRIVATE;
                List<String> whoCanAccess = config.getStringList(path + ".whoCanAccess");
                List<String> lore = config.getStringList(path + ".lore");
                ItemStack[] items = ((List<ItemStack>) config.get(path + ".inventory")).toArray(new ItemStack[0]);
                int slot = config.getInt(path + ".slot"); // Load slot

                BackpackObject backpack = new BackpackObject(ownerName, backpackName, iconMaterial, isExpanded, code, lore, items, slot, number);
                backpack.setPublicState(publicState);
                backpack.setWhoCanAccess(whoCanAccess != null ? new ArrayList<>(whoCanAccess) : new ArrayList<>());
                backpacks.add(backpack);

            }
            backpacksMap.put(playerName, backpacks);

            // Update backpackCounters
            if (!backpacks.isEmpty()) {
                backpackCounters.put(playerName, backpacks.size());
            }
        }
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&f[Backpacks] All the backpacks have been loaded up successfully"));
    }

    public static void saveEnderchests(FileConfiguration config) {
        for (Map.Entry<String, EnderChestObject> entry : Main.getEnderchestMap().entrySet()) {
            String playerName = entry.getKey();
            String path = playerName + ".enderchest";
            EnderChestObject enderChestObject = entry.getValue();

            config.set(path + ".size", enderChestObject.getSize());
            config.set(path + ".ownerName", enderChestObject.getOwnerName());

            // Save inventory contents
            List<Map<String, Object>> itemList = new ArrayList<>();
            for (ItemStack item : enderChestObject.getInventory().getContents()) {
                if (item != null) {
                    itemList.add(item.serialize());
                } else {
                    itemList.add(null);
                }
            }
            config.set(path + ".inventory", itemList);
        }
        Config.saveContentsConfig();

    }

    public static void loadEnderchests(FileConfiguration config) {
        if (config == null || config.getKeys(false).isEmpty()) {
            System.out.println("Config is empty. Nothing to load.");
            return;
        }

        for (String playerName : config.getKeys(false)) {
            String path = playerName + ".enderchest";
            if (config.getConfigurationSection(path) == null) {
                continue;
            }

            int size = config.getInt(path + ".size");
            String ownerName = config.getString(path + ".ownerName");
            List<Map<String, Object>> itemList = (List<Map<String, Object>>) config.getList(path + ".inventory");
            ItemStack[] inventoryContents = new ItemStack[size];

            // Deserialize items
            if (itemList != null) {
                for (int i = 0; i < itemList.size() && i < size; i++) {
                    if (itemList.get(i) != null) {
                        inventoryContents[i] = ItemStack.deserialize(itemList.get(i));
                    } else {
                        inventoryContents[i] = null;
                    }
                }
            }
            if (ownerName == null || ownerName.trim().isEmpty()) {
                continue; // Skip this player
            }
            EnderChestObject enderChestObject = new EnderChestObject(ownerName, size, inventoryContents);
            Main.getEnderchestMap().put(playerName, enderChestObject);

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (enderChestObject.getOwnerName().equalsIgnoreCase(onlinePlayer.getName().toLowerCase())) {
                    enderChestObject.setOwner(onlinePlayer);
                }
            }
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&f[Backpacks] All the enderchests have been loaded up successfully"));
        }
    }

    public static void loadBackpackSettings() {
        FileConfiguration config = Config.getContentsConfig();
            for (String playerName : config.getConfigurationSection("").getKeys(false)) {
                if (config.getBoolean(playerName + ".settings.returnToBackpackMenu")) {
                    BackpacksSettingsMenu.getReturnToBackpackMenu().add(playerName);
                    System.out.println();
                }
                if (config.getBoolean(playerName + ".settings.automaticBackpackExpansion")) {
                    BackpacksSettingsMenu.getAutomaticBackpackExpansion().add(playerName);
                }
            }
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&f[Backpacks] All the backpack settings have been initialized correctly"));
    }




    public static boolean isNotNumber(String str) {
        try {
            Double.parseDouble(str);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }

    public static Map<String, Integer> getBackpackCounters() {
        return backpackCounters;
    }
}


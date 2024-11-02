package manazzez.backpack;

//import de.rapha149.signgui.SignGUI;
import com.comphenix.protocol.ProtocolLib;
import manazzez.backpack.menu.PlayerMenuUtility;
import manazzez.backpack.utillities.BackpackManager;
import manazzez.backpack.utillities.BackpackObject;
import manazzez.backpack.utillities.EnderChestObject;
import manazzez.backpack.utillities.Listeners;
import manazzez.coins.Coins;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public final class Main extends JavaPlugin {

    private static final HashMap<Player, PlayerMenuUtility> playerMenuUtilityMap = new HashMap<>();
    private static final HashMap<String, ArrayList<BackpackObject>> backpacksMap = new HashMap<>();
    private static final HashMap<String, EnderChestObject> enderchestMap = new HashMap<>();
    private static ArrayList<String> backpackOwners = new ArrayList<>();
    private static HashMap<Player, Integer> finances = Coins.getMap();

    Coins coins = (Coins) Bukkit.getPluginManager().getPlugin("Coins");
    ProtocolLib lib = (ProtocolLib) Bukkit.getPluginManager().getPlugin("ProtocolLib");

    @Override
    public void onEnable() {
        Config.setupSettingsConfig();
        Config.setupContentsConfig();
        Config.loadDefaultsOnSettings();
        registerCommands();
        registerTabCompleter();
        registerEvents();
        loadBackpackOwners();
        BackpackManager.loadBackpacks();
        BackpackManager.loadBackpackSettings();
        BackpackManager.loadEnderchests(Config.getContentsConfig());
        if(!(coins != null && coins.isEnabled())) {
            getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&7You need to have your&e coins&7 plugin set up for this plugin to work properly"));
        }
        if(!(lib != null && lib.isEnabled())) {
            getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&7You need to have &bProtocolLib&7 installed for this plugin to work properly"));
        }
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&f[Backpacks] The backpack plugin has started successfully"));
    }
    @Override
    public void onDisable() {
        BackpackManager.saveBackpacks();
        BackpackManager.saveEnderchests(Config.getContentsConfig());
        Config.saveContentsConfig();
        Config.saveSettingsConfig();
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "The backpack plugin has shut down"));
    }

    public static PlayerMenuUtility getPlayerMenuUtility(Player player) {
        PlayerMenuUtility playerMenuUtility;
        if(playerMenuUtilityMap.containsKey(player)) {
            return playerMenuUtilityMap.get(player);
        } else {
            playerMenuUtility = new PlayerMenuUtility(player);
            playerMenuUtilityMap.put(player, playerMenuUtility);
            return playerMenuUtility;
        }

    }
    public static Main getInstance() {
        return getPlugin(Main.class); }
    public static HashMap<Player, Integer> getFinances() {
        return finances;
    }
    public static HashMap<String, ArrayList<BackpackObject>> getBackpacksMap() {
        return backpacksMap;
    }
    public static HashMap<String, EnderChestObject> getEnderchestMap() {return enderchestMap; }
    public static ArrayList<String> getBackpackOwners() {
        return backpackOwners;
    }
    public void loadBackpackOwners() {
        backpackOwners.addAll(Config.getContentsConfig().getKeys(false));
    }
    public void registerEvents() {
        getServer().getPluginManager().registerEvents(new Listeners(), this);
    }
    public void registerTabCompleter() {
        getCommand("Backpack").setTabCompleter(new Commands());
    }
    public void registerCommands() { getCommand("Backpack").setExecutor(new Commands()); }
}

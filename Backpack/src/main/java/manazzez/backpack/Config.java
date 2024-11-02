package manazzez.backpack;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Config {

    private static File contentsFile;
    private static FileConfiguration contentsFileConfiguration;

    private static File settingsFile;
    private static FileConfiguration settingsFileConfiguration;

    public static void setupContentsConfig() {
        contentsFile = new File(Bukkit.getServer().getPluginManager().getPlugin("Backpack").getDataFolder(), "Contents.yml");
        if (!contentsFile.exists()) {
            try {
                contentsFile.createNewFile();
                Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "The config file Contents has been created");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        contentsFileConfiguration = YamlConfiguration.loadConfiguration(contentsFile);
    }

    public static void setupSettingsConfig() {
        settingsFile = new File(Bukkit.getServer().getPluginManager().getPlugin("Backpack").getDataFolder(), "Settings.yml");
        if (!settingsFile.exists()) {
            try {
                settingsFile.createNewFile();
                Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "The config file Settings has been created");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        settingsFileConfiguration = YamlConfiguration.loadConfiguration(settingsFile);
    }


    public static void saveSettingsConfig() {
        try {
            settingsFileConfiguration.save(settingsFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveContentsConfig() {
        try {
            contentsFileConfiguration.save(contentsFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void loadDefaultsOnSettings() {
        getSettingsConfig().options().header("A Few options for customization. Made by Manazzez");
        getSettingsConfig().options().copyHeader(true);
        getSettingsConfig().addDefault("Settings.ExpansionPrice", 500);
        getSettingsConfig().addDefault("Settings.BackpackPrice", 500);
        getSettingsConfig().addDefault("Settings.Messages.HelpMessage", message1);
        getSettingsConfig().addDefault("Settings.Messages.HelpMessageStaff", message2);
        getSettingsConfig().options().copyDefaults(true);
        saveSettingsConfig();

    }

    public static FileConfiguration getSettingsConfig() {
        return settingsFileConfiguration;
    }

    public static FileConfiguration getContentsConfig() {
        return contentsFileConfiguration;
    }

    public static void reloadSettingsConfig() {
        settingsFileConfiguration = YamlConfiguration.loadConfiguration(settingsFile);
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&bSettings config reloaded successfully "));
    }

//

    public static void reloadContentsConfig() {
        contentsFileConfiguration = YamlConfiguration.loadConfiguration(contentsFile);
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&aContents config reloaded successfully "));
    }

    public static void loadSettingsConfig() {
        getSettingsConfig().addDefault("Purchase_Backpacks_Coins", 500);
    }

    static String message1 = "&&7Here's help: \n" +
            "&&7/&&abp&&7 &&a-&&7 Access the backpacks menu to open or edit your backpacks. \n" +
            "&&7/&&abp access&&7 <&&aplayer&&7> <&&abackpack&&7> &&a-&&7 Allows you to access someone's backpack if you're allowed. \n" +
            "&&7/&&abp purchase&&7 &&a-&&7 Purchase new backpacks, the limit is 14. \n" +
            "&&7/&&abp open&&7 <&&abackpack&&7> &&a-&&7 Open your backpacks with their number or name. \n" +
            "&&7/&&abp rename&&7 <&&abackpack&&7> ~ <&&anew name&&7> &&a-&&7 Rename your backpacks. \n" +
            "&&7/&&abp lore&&7 <&&abackpack&&7> ~ <&&alore ... lore&&7> &&a-&&7 Add subtext to the backpack icons in the menu. \n" +
            "&&7/&&abp expand&&7 <&&abackpack&&7> &&a-&&7 Expand non-expanded backpacks to 54 slots. \n" +
            "&&7/&&abp reset&&7 <&&abackpack&&7> &&a-&&7 Reset the customization on any of your backpacks.\n";

    static String message2 = "&&7Here's help: \n" +
            "&&7/&&abp&&7 &&a-&&7 Access the backpacks menu to open or edit your backpacks. \n" +
            "&&7/&&2bp view&&7 <&&2target&&7> &&2-&&7 View and interact with people's backpacks.\n" +
            "&&7/&&2bp reload&&7 &&a-&&7 Apply the changes you make to the config and save the backpacks.\n" +
            "&&7/&&abp access&&7 <&&aplayer&&7> <&&abackpack&&7> &&a-&&7 Allows you to access someone's backpack if you're allowed. \n" +
            "&&7/&&abp purchase&&7 &&a-&&7 Purchase new backpacks, the limit is 14. \n" +
            "&&7/&&abp open&&7 <&&abackpack&&7> &&a-&&7 Open your backpacks with their number or name. \n" +
            "&&7/&&abp rename&&7 <&&abackpack&&7> ~ <&&anew name&&7> &&a-&&7 Rename your backpacks. \n" +
            "&&7/&&abp lore&&7 <&&abackpack&&7> ~ <&&alore ... lore&&7> &&a-&&7 Add subtext to the backpack icons in the menu. \n" +
            "&&7/&&abp expand&&7 <&&abackpack&&7> &&a-&&7 Expand non-expanded backpacks to 54 slots. \n" +
            "&&7/&&abp reset&&7 <&&abackpack&&7> &&a-&&7 Reset the customization on any of your backpacks.";


//    public static void createCustomConfig() {
//        settingsFile = new File(Main.getInstance().getDataFolder(), "settings.yml");
//
//        if (!settingsFile.exists()) {
//            settingsFile.getParentFile().mkdirs();
//            try (BufferedWriter writer = new BufferedWriter(new FileWriter(settingsFile))) {
//                writer.write("# Settings for the backpacks plugin (Made by Manazzez)\n");
//                writer.write("Settings:\n");
//                writer.write("  BackpackPrice: 500\n");
//                writer.write("  ExpansionPrice: 500\n");
//                writer.write("  Messages:\n");
//                writer.write("    # Use the & to indicate the color you want to apply to the words/sentences\n");
//                writer.write("    # Use the pipe Icon (|) to indicate that the message will be multiple lines long\n");
//                writer.write("    # Then just skip to the next line naturally to continue making new lines\n");
//                writer.write("    HelpMessage: |\n");
//                writer.write("      \\&7Here's help:\n"); // Escape & with \\
//                writer.write("      \\&7/\\&abp\\&7 \\&a-\\&7 Access the backpacks menu to open or edit your backpacks.\n");
//                writer.write("      \\&7/\\&abp access\\&7 <\\&aplayer\\&7> <\\&abackpack\\&7> \\&a-\\&7 Allows you to access someone's backpack if you're allowed.\n");
//                writer.write("      \\&7/\\&abp purchase\\&7 \\&a-\\&7 Purchase new backpacks, the limit is 14.\n");
//                writer.write("      \\&7/\\&abp open\\&7 <\\&abackpack\\&7> \\&a-\\&7 Open your backpacks with their number or name.\n");
//                writer.write("      \\&7/\\&abp rename\\&7 <\\&abackpack\\&7> ~ <\\&anew name\\&7> \\&a-\\&7 Rename your backpacks.\n");
//                writer.write("      \\&7/\\&abp lore\\&7 <\\&abackpack\\&7> ~ <\\&alore ... lore\\&7> \\&a-\\&7 Add subtext to the backpack icons in the menu.\n");
//                writer.write("      \\&7/\\&abp expand\\&7 <\\&abackpack\\&7> \\&a-\\&7 Expand non-expanded backpacks to 54 slots.\n");
//                writer.write("      \\&7/\\&abp reset\\&7 <\\&abackpack\\&7> \\&a-\\&7 Reset the customization on any of your backpacks.\n");
//                writer.write("    # If he has the permission \"backpacks.adminViewMenu\" some more options will be available to him\n");
//                writer.write("    # Which are: /bp view and /bp reload. Take that into consideration when designing the messages\n");
//                writer.write("    HelpMessageStaff: |\n");
//                writer.write("      \\&7Here's help:\n");
//                writer.write("      \\&7/\\&abp\\&7 \\&a-\\&7 Access the backpacks menu to open or edit your backpacks.\n");
//                writer.write("      \\&7/\\&abp view\\&7 <\\&2target\\&7> \\&2-\\&7 View and interact with people's backpacks.\n");
//                writer.write("      \\&7/\\&abp reload\\&7 <\\&2reload\\&7> \\&a-\\&7 Apply the changes you make to the config and save the backpacks.\n");
//                writer.write("      \\&7/\\&abp access\\&7 <\\&aplayer\\&7> <\\&abackpack\\&7> \\&a-\\&7 Allows you to access someone's backpack if you're allowed.\n");
//                writer.write("      \\&7/\\&abp purchase\\&7 \\&a-\\&7 Purchase new backpacks, the limit is 14.\n");
//                writer.write("      \\&7/\\&abp open\\&7 <\\&abackpack\\&7> \\&a-\\&7 Open your backpacks with their number or name.\n");
//                writer.write("      \\&7/\\&abp rename\\&7 <\\&abackpack\\&7> ~ <\\&anew name\\&7> \\&a-\\&7 Rename your backpacks.\n");
//                writer.write("      \\&7/\\&abp lore\\&7 <\\&abackpack\\&7> ~ <\\&alore ... lore\\&7> \\&a-\\&7 Add subtext to the backpack icons in the menu.\n");
//                writer.write("      \\&7/\\&abp expand\\&7 <\\&abackpack\\&7> \\&a-\\&7 Expand non-expanded backpacks to 54 slots.\n");
//                writer.write("      \\&7/\\&abp reset\\&7 <\\&abackpack\\&7> \\&a-\\&7 Reset the customization on any of your backpacks.\n");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }


}


package manazzez.backpack;

import lombok.Getter;
import manazzez.backpack.menu.menus.*;
import manazzez.backpack.utillities.BackpackObject;
import manazzez.backpack.utillities.PublicState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

public class Commands implements CommandExecutor, TabCompleter {

    @Getter
    private static final HashMap<Player, String> targetMap = new HashMap<>();
    private ArrayList<String> check = new ArrayList();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You cannot use this command");
            return true;
        }
        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("Backpacks")) {
            if (!player.hasPlayedBefore()) {
                ArrayList<BackpackObject> playerList = new ArrayList<>();
                Main.getBackpacksMap().put(player.getName().toLowerCase(), playerList);
            }
            if (!Main.getBackpacksMap().containsKey(player.getName().toLowerCase())) {
                ArrayList<BackpackObject> playerList = new ArrayList<>();
                Main.getBackpacksMap().put(player.getName().toLowerCase(), playerList);
            }
            if (Main.getBackpacksMap().containsKey(player.getName().toLowerCase())) {
                for (BackpackObject backpack : Main.getBackpacksMap().get(player.getName().toLowerCase())) {
                    backpack.setOwner(player);
                }
            }
            if (args.length == 0) {
                BackpacksMenu backpacksMenu = new BackpacksMenu(Main.getPlayerMenuUtility(player));
                BackpacksMenu.clickedBackpack.remove(player);
                backpacksMenu.open();
                return true;
            }
            //Only needs 1 argument for it to be executed successfully
            if (args.length == 1 && args[0].equalsIgnoreCase("purchase")) {
                ArrayList<BackpackObject> backpackObject = Main.getBackpacksMap().get(player.getName().toLowerCase());
                if (backpackObject.size() < 14) {
                    ConfirmPurchaseMenu confirmPurchaseMenu = new ConfirmPurchaseMenu(Main.getPlayerMenuUtility(player));
                    confirmPurchaseMenu.open();
                    player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1, 1);
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&7You have already bought the maximum amount of backpacks available."));
                    player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1, 1);
                }
                return true;
            } else if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                if (!player.hasPermission("backpacks.adminViewMenu")) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&7You can't use this command"));
                } else {
                    Config.reloadContentsConfig();
                    Config.reloadSettingsConfig();
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&7The &bSettings config&7 and &bContents config&7 have been reloaded successfully!"));
                }
                return true;

            } else if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
                if (!(player.hasPermission("backpacks.adminViewMenu"))) {
                    String message = Config.getSettingsConfig().getString("Settings.Messages.HelpMessage");
                    message = message.replace("&&", "&");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));

                } else {
                    String message = Config.getSettingsConfig().getString("Settings.Messages.HelpMessageStaff");
                    message = message.replace("&&", "&");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                }
                return true;
                //Logic to handle if the player sends only 1 argument after command:
            } else if (args.length == 1 && args[0].equalsIgnoreCase("view")) {
                if (!(player.hasPermission("backpacks.adminViewMenu"))) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&7You can't use this command"));
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&cImproper usage.&7 You need to provide a player name afterwards to inspect his backpacks"));
                }
                return true;

            } else if (args.length == 1 && args[0].equalsIgnoreCase("open")) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cImproper usage.&7 You need to provide the number of the backpack you want to access after the '&copen&7'"));
                return true;
            } else if (args.length == 1 && args[0].equalsIgnoreCase("rename")) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cImproper usage.&7 You need to provide the number of the backpack you want to access after the '&crename&7' and it's new name after said number"));
                return true;
            } else if (args.length == 1 && args[0].equalsIgnoreCase("lore")) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cImproper usage.&7 You need to provide the number of the backpack you want to access after '&clore&7' \n" +
                                "&7After that you can use 3 dots (...) spaced from the words to indicate the next line"));
                return true;
            } else if (args.length == 1 && args[0].equalsIgnoreCase("expand")) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cImproper usage.&7 You need to provide the number of the backpack you want to expand after '&cexpand&7'"));
                return true;
            } else if (args.length == 1 && args[0].equalsIgnoreCase("reset")) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cImproper usage.&7 You need to provide the number of the backpack you want to reset after '&creset&7'"));
                return true;
            }
            //end of checks

            //Actual logic for the commands
            if (args.length == 2 && args[0].equalsIgnoreCase("view")) {
                if (!player.hasPermission("backpacks.adminViewMenu")) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&7You can't use this command"));
                    return true;
                }

                boolean playerFound = false;

                for (String targetName : Config.getContentsConfig().getKeys(false)) {
                    for (Map.Entry<String, ArrayList<BackpackObject>> entry : Main.getBackpacksMap().entrySet()) {
                        String playerName = entry.getKey();
                        if (targetName.equalsIgnoreCase(playerName)) {
                            playerFound = true;
                            if (handleTargetName(playerName, player, args, check)) {
                                return true;
                            }
                        }
                    }
                    if (handleTargetName(targetName, player, args, check)) {
                        return true;
                    }
                }

                if (!playerFound) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&7The player you've provided is not in our backpack database. Probably a typo"));
                    return true;
                }

            } else if (args.length == 2 && args[0].equalsIgnoreCase("expand")) {
                for (BackpackObject backpackObject : Main.getBackpacksMap().get(player.getName().toLowerCase())) {
                    if (isNotNumber(args[1])) {
                        String backpackName = ChatColor.stripColor(backpackObject.getBackpackName());
                        if (backpackObject.getBackpackName().equalsIgnoreCase(backpackName)) {
                            BackpacksMenu.clickedBackpack.put(player, backpackObject.getNumber());
                            ConfirmExpansionMenu confirmExpansionMenu = new ConfirmExpansionMenu(Main.getPlayerMenuUtility(player));
                            confirmExpansionMenu.open();
                        }
                    }
                    int inputNumber = Integer.parseInt(args[1]);
                    if (inputNumber > 14) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&cImproper usage.&7 You can't get any new backpacks after purchasing 14 of them."));
                        return true;
                    }
                    if (Main.getBackpacksMap().get(player.getName().toLowerCase()).size() < inputNumber) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&cImproper usage.&7 The number you gave is greater than the number of any of the backpacks you own"));
                        return true;
                    }
                    if (backpackObject.getNumber() == inputNumber) {
                        if (backpackObject.isExpanded()) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    "&7This backpack is already expanded!"));
                        } else {
                            ConfirmExpansionMenu confirmExpansionMenu = new ConfirmExpansionMenu(Main.getPlayerMenuUtility(player));
                            BackpacksMenu.clickedBackpack.put(player, backpackObject.getNumber());
                            confirmExpansionMenu.open();
                        }
                        return true;
                    }

                }
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cImproper usage.&7 You need to input one of your backpack numbers or name after '&cexpand&7'"));
                return true;

            } else if (args.length == 2 && args[0].equalsIgnoreCase("reset")) {


                for (BackpackObject backpackObject : Main.getBackpacksMap().get(player.getName().toLowerCase())) {
                    String backpackName = ChatColor.stripColor(backpackObject.getBackpackName());
                    if (isNotNumber(args[1])) {
                        if (backpackObject.getBackpackName().equalsIgnoreCase(backpackName)) {
                            BackpacksMenu.clickedBackpack.put(player, backpackObject.getNumber());
                            ConfirmResetMenu confirmResetMenu = new ConfirmResetMenu(Main.getPlayerMenuUtility(player));
                            confirmResetMenu.open();
                        }
                        return true;
                    }
                    int inputNumber = Integer.parseInt(args[1]);
                    if (inputNumber > 14) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&cImproper usage.&7 The maximum number of backpacks is 14."));
                        return true;
                    }
                    if (Main.getBackpacksMap().get(player.getName().toLowerCase()).size() < inputNumber) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&cImproper usage.&7 The number you gave is greater than the number of any of the backpacks you own"));
                        return true;
                    }
                    if (backpackObject.getNumber() == inputNumber) {
                        ConfirmResetMenu confirmResetMenu = new ConfirmResetMenu(Main.getPlayerMenuUtility(player));
                        BackpacksMenu.clickedBackpack.put(player, backpackObject.getNumber());
                        confirmResetMenu.open();
                        return true;
                    }
                }
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cImproper usage.&7 You need to input one of your backpack numbers or name after '&creset&7'"));
                return true;

            } else if (args.length == 2 && args[0].equalsIgnoreCase("open")) {
                for (BackpackObject backpackObject : Main.getBackpacksMap().get(player.getName().toLowerCase())) {
                    if (isNotNumber(args[1])) {
                        String backpackName = ChatColor.stripColor(backpackObject.getBackpackName());
                        if (backpackObject.getBackpackName().equalsIgnoreCase(backpackName)) {
                            backpackObject.open(player);
                        } else {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    "&7You don't own any backpack named:&c " + args[1]));
                        }
                        return true;
                    }
                    int inputNumber = Integer.parseInt(args[1]);
                    if (inputNumber > 14) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&cImproper usage.&7 The maximum number of backpacks is 14."));
                        return true;
                    }
                    if (Main.getBackpacksMap().get(player.getName().toLowerCase()).size() < inputNumber) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&cImproper usage.&7 The number you gave is greater than the number of any of the backpacks you own"));
                        return true;
                    }
                    if (backpackObject.getNumber() == inputNumber) {
                        backpackObject.open(player);
                        return true;
                    }
                }
            } else if (args[0].equalsIgnoreCase("rename")) {
                if (args.length < 3) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&cImproper usage.&7 You need to specify a name for your backpack. It can also be multiple words long.\n" +
                                    "&7If you want to rename a backpack using its name, use a &b~&7 symbol to differentiate its old name and new name afterwards"));
                    return true;
                }

                String fullArgument = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                String[] parts = fullArgument.split("~", 2);

                if (parts.length < 2 || parts[1].trim().isEmpty()) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&cImproper usage.&7 You need to specify the new name after the old name separated by a ~, and it cannot be empty."));
                    return true;
                }

                String oldNameOrNumber = parts[0].trim();
                String newName = parts[1].trim();

                if (newName.isEmpty()) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&cImproper usage.&7 The new name cannot be empty."));
                    return true;
                }

                for (BackpackObject backpackObject : Main.getBackpacksMap().get(player.getName().toLowerCase())) {
                    String cleanBackpackName = ChatColor.stripColor(backpackObject.getBackpackName());

                    // Check if the new name is already in use
                    boolean nameExists = Main.getBackpacksMap().get(player.getName().toLowerCase()).stream()
                            .anyMatch(b -> b.getBackpackName().equalsIgnoreCase(newName));

                    if (nameExists) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&cImproper usage.&7 There is already a backpack with the name: " + newName));
                        return true;
                    }

                    if (isNotNumber(oldNameOrNumber)) {
                        String cleanUserInput = removeCharacterAndNext(oldNameOrNumber, '&');

                        if (cleanBackpackName.equalsIgnoreCase(cleanUserInput)) {
                            backpackObject.setBackpackName(newName);
                            player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1, 1);
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    "&7You've changed the name of your backpack&b " + backpackObject.getNumber() + "&7 to: " + backpackObject.getBackpackName()));
                            return true;
                        }
                    } else {
                        try {
                            int inputNumber = Integer.parseInt(oldNameOrNumber);

                            if (inputNumber > 14) {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                        "&cImproper usage.&7 The maximum number of backpacks is 14."));
                                return true;
                            }

                            if (backpackObject.getNumber() == inputNumber) {
                                backpackObject.setBackpackName(newName);
                                player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1, 1);
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                        "&7You've changed the name of your backpack&b " + backpackObject.getNumber() + "&7 to: " + backpackObject.getBackpackName()));
                                return true;
                            }
                        } catch (NumberFormatException e) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    "&cImproper usage.&7 Please provide a valid backpack name or number."));
                            return true;
                        }
                    }
                }

                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&7You don't own any backpack named or numbered: &c" + oldNameOrNumber));
                return true;
            } else if (args[0].equalsIgnoreCase("lore")) {
                if (args.length < 3) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&cImproper usage.&7 You need to specify the lore for your backpack \n" +
                                    "&7It can also have multiple lines, just put 3 dots (...) \n " +
                                    "&7where you want to skip to the next line"));
                    return true;
                } else {
                    for (BackpackObject backpackObject : Main.getBackpacksMap().get(player.getName().toLowerCase())) {
                        String[] arguments = Arrays.copyOfRange(args, 2, args.length);
                        String rawLore = String.join(" ", arguments);
                        String ellipsisPattern = "\\.\\.\\.";
                        String[] mediumLore = rawLore.split(ellipsisPattern);

                        ArrayList<String> lore = new ArrayList<>();
                        for (String line : mediumLore) {
                            if (!line.contains("&")) {
                                line = "&7" + line;
                            } else {
                                line = "&7" + line.substring(line.indexOf("&"));
                            }
                            lore.add(line);
                        }
                        if (isNotNumber(args[1])) {
                            String inputName = ChatColor.stripColor(backpackObject.getBackpackName());
                            if (backpackObject.getBackpackName().equalsIgnoreCase(inputName)) {
                                backpackObject.setLore(lore);
                                player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1, 1);
                                player.sendMessage(ChatColor.translateAlternateColorCodes(
                                        '&', "&7You've changed the &blore&7 on your backpack &b" + backpackObject.getNumber()));
                            } else {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                        "&7You don't own any backpack named:&c " + args[1]));
                            }
                            return true;
                        }
                        int inputNumber = Integer.parseInt(args[1]);
                        if (inputNumber > 14) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    "&cImproper usage.&7 The maximum number of backpacks is 14."));
                            return true;
                        }
                        if (Main.getBackpacksMap().get(player.getName().toLowerCase()).size() < inputNumber) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    "&cImproper usage.&7 The number you gave is greater than the number of any of the backpacks you own"));
                            return true;
                        }
                        if (backpackObject.getNumber() == inputNumber) {
                            backpackObject.setLore(lore);
                            player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1, 1);
                            player.sendMessage(ChatColor.translateAlternateColorCodes(
                                    '&', "&7You've changed the &blore&7 on your backpack &b" + backpackObject.getNumber()));
                            return true;
                        }
                    }
                }
            } else if (args[0].equalsIgnoreCase("access")) {
                if (args.length == 1) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&7You need to provide the &cname&7 of the player who's backpack you want to access."));
                    return true;
                } else if (args.length == 2) {
                    if (!Main.getBackpacksMap().containsKey(args[1].toLowerCase())) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&7The player &c" + args[1] + "&7 has not played here before."));
                        return true;
                    }
                    ArrayList<String> accessibleBackpacks = new ArrayList<>();
                    for (BackpackObject backpackObject : Main.getBackpacksMap().get(args[1].toLowerCase())) {
                        PublicState publicState = backpackObject.getPublicState();
                        if (publicState.equals(PublicState.ANYONE)) {
                            accessibleBackpacks.add(backpackObject.getBackpackName());
                        } else if (publicState.equals(PublicState.HANDPICKED)) {
                            if (backpackObject.getWhoCanAccess().contains(player.getName().toLowerCase())) {
                                accessibleBackpacks.add(backpackObject.getBackpackName());
                            }
                        }
                    }
                    String target = AdminViewerMenu.capitalizeFirstLetter(args[1]);
                    if (player.getName().equalsIgnoreCase(target)) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&7These are your own backpacks. Just use &a/backpack"));
                        return true;
                    }
                    if (accessibleBackpacks.isEmpty()) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&7You can't access any of &c" + target + "'s&7 backpacks"));
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&7You have access to &a" + target + "'s&7 following backpacks"));
                        for (String backpackName : accessibleBackpacks) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    "&2- &a" + backpackName));
                        }
                    }
                    return true;

                } else {
                    String playerName = args[1];
                    StringBuilder backpackIdentifierBuilder = new StringBuilder();
                    for (int i = 2; i < args.length; i++) {
                        backpackIdentifierBuilder.append(args[i]);
                        if (i < args.length - 1) {
                            backpackIdentifierBuilder.append(" ");
                        }
                    }
                    String backpackIdentifier = backpackIdentifierBuilder.toString();
                    handleBackpackAccess(playerName, backpackIdentifier, player);
                    return true;
                }
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        List<String> suggestions = new ArrayList<>();
        ArrayList<String> configNames = Main.getBackpackOwners();
        configNames.remove("settings");

        if (!(sender instanceof Player)) return new ArrayList<>();
        Player player = (Player) sender;

        if (args.length == 1) {
            if (player.hasPermission("backpacks.adminViewMenu")) {
                suggestions.addAll(Arrays.asList("view", "reload", "access", "purchase", "help", "open", "rename", "lore", "expand", "reset"));
            } else {
                suggestions.addAll(Arrays.asList("access", "purchase", "help", "open", "rename", "lore", "expand", "reset"));
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("view") && player.hasPermission("backpacks.adminViewMenu")) {
            suggestions.addAll(configNames);
        }

        return suggestions;
    }

    public static boolean isNotNumber(String str) {
        try {
            Double.parseDouble(str);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }

    private boolean handleTargetName(String targetName, Player player, String[] args, ArrayList<String> check) {
        if (targetName.equalsIgnoreCase(args[1])) {
            if (args[1].equalsIgnoreCase(player.getName()) && !check.contains(player.getName().toLowerCase())) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&7Why would you want to check your backpacks in the admin menu? Send this command again to do so."));
                check.add(player.getName().toLowerCase());
                return true;
            } else if (args[1].equalsIgnoreCase(player.getName()) && check.contains(player.getName().toLowerCase())) {
                ((Map<Player, String>) Commands.targetMap).put(player, args[1].toLowerCase());
                openAdminViewerMenu(player);
                return true;
            }
            if (!args[1].equalsIgnoreCase(player.getName())) {
                ((Map<Player, String>) Commands.targetMap).put(player, args[1].toLowerCase());
                openAdminViewerMenu(player);
                return true;
            }
        }
        return false;
    }

    private void openAdminViewerMenu(Player player) {
        AdminViewerMenu adminViewerMenu = new AdminViewerMenu(Main.getPlayerMenuUtility(player));
        AdminViewerMenu.getAdminInUse().add(player.getName().toLowerCase());
        adminViewerMenu.open();
    }

    public String removeCharacterAndNext(String input, char character) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == character) {
                i++; // Skip the next character as well
            } else {
                result.append(input.charAt(i));
            }
        }
        return result.toString();
    }

    private boolean isValidBackpackNumber(int number, Player player) {
        if (number > 14) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou can't have more than 14 backpacks."));
            return false;
        }

        for (BackpackObject backpackObject : Main.getBackpacksMap().get(player.getName().toLowerCase())) {
            if (backpackObject.getNumber() == number) {
                return true;
            }
        }
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou don't own a backpack with that number."));
        return false;
    }

    private void handleBackpackAccess(String playerName, String backpackIdentifier, Player player) {
        if (!Main.getBackpacksMap().containsKey(playerName.toLowerCase())) {
            String nickname = AdminViewerMenu.capitalizeFirstLetter(playerName);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7The player &c" + nickname + "&7 has not played here before"));
            return;
        }
        List<BackpackObject> backpacks = Main.getBackpacksMap().get(playerName.toLowerCase());
        boolean backpackFound = false;
        for (BackpackObject backpackObject : backpacks) {
            if (isNotNumber(backpackIdentifier)) {
                String backpackName = ChatColor.stripColor(backpackObject.getBackpackName());
                String[] backpackWords = backpackName.split(" ");
                String[] identifierWords = backpackIdentifier.split(" ");
                if (identifierWords.length == backpackWords.length) {
                    boolean match = true;
                    for (int i = 0; i < backpackWords.length; i++) {
                        if (!identifierWords[i].equalsIgnoreCase(backpackWords[i])) {
                            match = false;
                            break;
                        }
                    }
                    if (match) {
                        processBackpackAccess(player, backpackObject);
                        backpackFound = true;
                        break;
                    }
                }
            } else {
                try {
                    int number = Integer.parseInt(backpackIdentifier);
                    if (number > 14) {
                        sendMessage(player, "&cImproper usage.&7 The maximum number of backpacks is 14.");
                        return;
                    }
                    if (backpackObject.getNumber() == number) {
                        processBackpackAccess(player, backpackObject);
                        backpackFound = true;
                        break;
                    }
                } catch (NumberFormatException e) {
                    sendMessage(player, "&cImproper usage.&7 Please provide a valid backpack name or number.");
                    return;
                }
            }
        }
        String nickname = ChatColor.RED + AdminViewerMenu.capitalizeFirstLetter(playerName);
        if (!backpackFound) {
            sendMessage(player, nickname + "&7 doesn't own any backpack named or numbered: &c" + backpackIdentifier);
        }
    }

    private void processBackpackAccess(Player player, BackpackObject backpackObject) {
        if (backpackObject.getPublicState().equals(PublicState.PRIVATE)) {
            sendMessage(player, "&7You cannot access this backpack");
        } else if (backpackObject.getPublicState().equals(PublicState.HANDPICKED)) {
            if (!backpackObject.getWhoCanAccess().contains(player.getName().toLowerCase())) {
                sendMessage(player, "&7You cannot access this backpack");
            } else {
                backpackObject.open(player);
                player.playSound(player.getLocation(), Sound.CHEST_OPEN, 1, 1);
            }
        } else {
            backpackObject.open(player);
            player.playSound(player.getLocation(), Sound.CHEST_OPEN, 1, 1);
        }
    }

    private void sendMessage(Player player, String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }


}

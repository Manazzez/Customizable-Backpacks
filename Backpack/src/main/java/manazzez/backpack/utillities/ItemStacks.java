package manazzez.backpack.utillities;

import manazzez.backpack.Config;
import manazzez.backpack.Main;
import manazzez.backpack.menu.menus.AdminViewerMenu;
import manazzez.backpack.menu.menus.BackpacksMenu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ItemStacks {

    public static ItemStack pane() {
        ItemStack pane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
        ItemMeta paneMeta = pane.getItemMeta();
        paneMeta.setDisplayName(ChatColor.WHITE + "Locked slot");
        paneMeta.setLore(Arrays.asList(ChatColor.GRAY + "This slot is locked", ChatColor.GRAY + "Click it to purchase a new backpack"));
        pane.setItemMeta(paneMeta);
        return pane;
    }

    public static ItemStack chest() {
        ItemStack chest = new ItemStack(Material.CHEST);
        ItemMeta chestMeta = chest.getItemMeta();
        chestMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "New backpack");
        chestMeta.setLore(Collections.singletonList(ChatColor.GRAY + "Finish the payment by clicking the green wool"));
        chestMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
        chestMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        chest.setItemMeta(chestMeta);
        return chest;
    }

    public static ItemStack greenWool() {
        int value = Config.getSettingsConfig().getInt("Settings.BackpackPrice");
        ItemStack item = new ItemStack(Material.WOOL, 1, (short) 13);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Confirm purchase");
        meta.setLore(Collections.singletonList(ChatColor.translateAlternateColorCodes('&', "&7By doing so &e" + value + "&7 coins will be deducted from your account")));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack grayWool() {
        int value = Config.getSettingsConfig().getInt("Settings.BackpackPrice");
        ItemStack item = new ItemStack(Material.WOOL, 1, (short) 7);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + "Confirm purchase");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "You don't have the " + ChatColor.YELLOW + value + ChatColor.GRAY + " coins needed", ChatColor.GRAY + "get more coins to purchase this"));
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack redWool() {
        ItemStack item = new ItemStack(Material.WOOL, 1, (short) 14);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Decline purchase");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "If you changed your mind just click this item"));
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack greenPane() {
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 13);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Return");
        meta.setLore(Collections.singletonList(ChatColor.GRAY + "Return to the backpacks menu"));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack resetBackpack() {
        ItemStack item = new ItemStack(Material.HOPPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Reset Backpack");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "Press this item to reset you backpack", ChatColor.GRAY + "to it's default format"));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack notExpandedBackpack() {
        int price = Config.getSettingsConfig().getInt("Settings.ExpansionPrice");
        ItemStack item = new ItemStack(Material.MINECART);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "Expand Backpack");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "You can expand your backpack to 54 slots"
                , ChatColor.GRAY + "This will cost you " + ChatColor.YELLOW + price + ChatColor.GRAY + " coins"));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack expandedBackpack() {
        ItemStack item = new ItemStack(Material.STORAGE_MINECART);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "Backpack already expanded");
        meta.setLore(Collections.singletonList(ChatColor.GRAY + "This backpack has already been expanded"));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack defineLore() {
        ItemStack item = new ItemStack(Material.BOOK_AND_QUILL);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Add lore");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "If you  want to you can add lore to you backpack",
                ChatColor.GRAY + "A sign GUI will show up to you. ",
                ChatColor.GRAY + "You can use all 4 lines to define the lore",
                ChatColor.GRAY + "This text right here is the lore!"));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack renameBackpack() {
        ItemStack item = new ItemStack(Material.SIGN);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_PURPLE + "Rename Backpack");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "Rename this backpack to your liking!", ChatColor.GRAY + "You can also color It using color codes (&)"));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack changeIcon(Player player) {
        ArrayList<BackpackObject> backpackObjects = Main.getBackpacksMap().get(player.getName().toLowerCase());
        for (BackpackObject playerBackpack : backpackObjects) {
            if (playerBackpack.getNumber() == BackpacksMenu.clickedBackpack.get(player)) {
                ItemStack item = playerBackpack.getIconItemStack();
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.AQUA + "Change Icon");
                meta.setLore(Arrays.asList(ChatColor.GRAY + "Right click this item to", ChatColor.GRAY + "change this backpack's icon"));
                item.setItemMeta(meta);
                return item;
            }
        }
        return null;
    }

    public static ItemStack customIcon() {
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + "Pick a custom Icon");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "Left click this with the item you want to define as a custom icon",
                ChatColor.GRAY + "If you want to change it eventually just right click it to default it",
                ChatColor.YELLOW + "WARNING: " + ChatColor.GRAY + "The item that you use to define as an icon will be dropped on the ground."));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack enderChest() {
        ItemStack item = new ItemStack(Material.ENDER_CHEST);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Ender Chest");
        meta.setLore(Collections.singletonList(ChatColor.GRAY + "Access your enderchest remotely"));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack question() {
        String string = "http://textures.minecraft.net/texture/9e5bb8b31f46aa9af1baa88b74f0ff383518cd23faac52a3acb96cfe91e22ebc";
        ItemStack item = SkullCreator.itemFromUrl(string);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + "Here's how it works");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "Click on the panes to purchase new backpacks",
                ChatColor.GRAY + "Left clicking your backpacks will open them", ChatColor.GRAY + "And right clicking them will open the backpack editor menu",
                ChatColor.GRAY + "You can also manage some other settings by clicking in the gear icon"));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack gear() {
        String string = "http://textures.minecraft.net/texture/ec2ff244dfc9dd3a2cef63112e7502dc6367b0d02132950347b2b479a72366dd";
        ItemStack item = SkullCreator.itemFromUrl(string);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + "Configurations");
        meta.setLore(Collections.singletonList(ChatColor.GRAY + "Here's some general configurations for the backpacks"));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack warning() {
        String string = "http://textures.minecraft.net/texture/dd2acd9f2dfc2e05f69d941fe9970e8c3f05527a02a9381157891c8ddb8cf3";
        ItemStack item = SkullCreator.itemFromUrl(string);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Warning!");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "This will reset this backpack's customization", ChatColor.GRAY + "All your itens will still be here afterwards"));
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack lightOff() {
        String string = "http://textures.minecraft.net/texture/b76230a0ac52af11e4bc84009c6890a4029472f3947b4f465b5b5722881aacc7";
        ItemStack item = SkullCreator.itemFromUrl(string);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + "Return from backpack");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "By turning this on you'll always", ChatColor.GRAY + "return to the backpacks menu", ChatColor.GRAY + "after closing your backpack"));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack lightOn() {
        String string = "http://textures.minecraft.net/texture/32ff8aaa4b2ec30bc5541d41c8782199baa25ae6d854cda651f1599e654cfc79";
        ItemStack item = SkullCreator.itemFromUrl(string);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Return from backpack");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "By turning this off you'll stop", ChatColor.GRAY + "returning to the backpacks menu", ChatColor.GRAY + "after closing your backpack"));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack cart() {
        ItemStack item = new ItemStack(Material.STORAGE_MINECART);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "Expand backpack");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "By finishing this purchase your backpack ", ChatColor.GRAY + "will be upgraded to a 54 slot backpack"));
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack returnArrow() {
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + "Return");
        meta.setLore(Collections.singletonList(ChatColor.GRAY + "Return to the backpacks menu"));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack autoExpandBackpacks() {
        ItemStack item = new ItemStack(Material.STORAGE_MINECART);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "Expanded backpacks");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "Automatically purchase expanded backpacks", ChatColor.GRAY + "If you have the needed money"));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack goldenGear() {
        String string = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTk0OWExOGNiNTJjMjkzZmU3ZGU3YmExMDE0NjcxMzQwZWQ3ZmY4ZTVkNzA1YjJkNjBiZjg0ZDUzMTQ4ZTA0In19fQ==";
        ItemStack item = SkullCreator.itemFromBase64(string);
        ItemMeta meta = item.getItemMeta();
        return item;
    }

    public static ItemStack light(boolean active) {
        if (active) {
            String string = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWM1YThhYThhNGMwMzYwMGEyYjVhNGViNmJlYjUxZDU5MDI2MGIwOTVlZTFjZGFhOTc2YjA5YmRmZTU2NjFjNiJ9fX0=";
            ItemStack item = SkullCreator.itemFromBase64(string);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.RED + "Return to view");
            meta.setLore(Arrays.asList(ChatColor.GRAY + "By turning this off you'll no longer", ChatColor.GRAY + "return to the admin view menu after", ChatColor.GRAY + "closing someones backpack"));
            item.setItemMeta(meta);
            return item;
        } else {
            String string = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjc2MjMwYTBhYzUyYWYxMWU0YmM4NDAwOWM2ODkwYTQwMjk0NzJmMzk0N2I0ZjQ2NWI1YjU3MjI4ODFhYWNjNyJ9fX0=";
            ItemStack item = SkullCreator.itemFromBase64(string);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.WHITE + "");
            meta.setLore(Arrays.asList(ChatColor.GRAY + "By turning this on you'll", ChatColor.GRAY + "return to the admin view menu after", ChatColor.GRAY + "closing someones backpack"));
            item.setItemMeta(meta);
            return item;
        }
    }

    public static ItemStack returnEnderChest() {
        ItemStack item = new ItemStack(Material.ENDER_CHEST);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_PURPLE + "Return from enderchest");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "You'll return to the backpacks menu", ChatColor.GRAY + "after closing your enderchest.", ChatColor.GRAY + "This will not affect physical enderchests"));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack noEnderchest() {
        ItemStack item = new ItemStack(Material.OBSIDIAN);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_GRAY + "No Enderchest");
        meta.setLore(Collections.singletonList(ChatColor.GRAY + "This player has not activated his enderchest yet"));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack backpackAccessibility(BackpackObject backpackObject) {
        ItemStack item;

        if (!backpackObject.getPublicState().equals(PublicState.PRIVATE)) {
            item = null;

            switch (backpackObject.getPublicState()) {
                case ANYONE:
                    item = everyoneHead(true);
                    break;
                case FRIENDS:
                    item = friendsHead(true);
                    break;
                case GUILD:
                    item = guildHead(true);
                    break;
                case HANDPICKED:
                    item = handpickedHead(true);
                    break;
            }

            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.DARK_AQUA + "Public backpack");
            meta.setLore(Arrays.asList(
                    ChatColor.GRAY + "This backpack is currently accessible to: " + ChatColor.DARK_AQUA + capitalizeFirstLetter(backpackObject.getPublicState().toString()),
                    ChatColor.GRAY + "To private it to everyone or change who can access it",
                    ChatColor.GRAY + "You can left click here to open the publicness menu."
            ));
            item.setItemMeta(meta);
        } else {
            item = privateBackpack(true);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.DARK_GRAY + "Private backpack");
            meta.setLore(Arrays.asList(
                    ChatColor.GRAY + "By activating this you will make this specific backpack public to other players.",
                    ChatColor.GRAY + "You can also define what group of players is able to access it by left-clicking this",
                    ChatColor.GRAY + "With said group being: your friends, faction members, and everybody"
            ));
            item.setItemMeta(meta);
        }

        return item;
    }


    public static ItemStack toggle(boolean active) {
        ItemStack item = new ItemStack(Material.INK_SACK);
        ItemMeta meta = item.getItemMeta();
        if (active) {
            meta.setDisplayName(ChatColor.GREEN + "Enabled");
            item.setDurability((short) 10);
        } else {
            meta.setDisplayName(ChatColor.GRAY + "Disabled");
            item.setDurability((short) 8);
        }
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack interactOn() {
        String string = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjFmYWY2YWM2YzNkZTA5Njk2MTRmZGRhN2ZiNDdiYjgxZGI3NzU0Y2ZiNmY0ZDFjN2VjZDE0M2U1NTViMzlkYSJ9fX0=\n";
        ItemStack item = SkullCreator.itemFromBase64(string);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Interact with itens");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "Interact with itens is turned on.", ChatColor.GRAY + "You'll be able to put and remove itens", ChatColor.GRAY + "from player's backpacks, careful."));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack interactOff() {
        String string = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2Y4NTM3MjU4MjU0NmMwMjFjODk3ODE5MTQwNzRkNzFkNzM1NjU2MjNhYzM4ZTA0YmViZWRhMDUyZDUwMjE0ZCJ9fX0=";
        ItemStack item = SkullCreator.itemFromBase64(string);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + "Interact with itens");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "By turning this on you will be able to", ChatColor.GRAY + "access and interact with this players Itens.", ChatColor.GRAY + "It's advised for you no to do that, in most situations."));
        item.setItemMeta(meta);
        return item;
    }

    public static String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

    public static ItemStack everyoneHead(boolean active) {
        ItemStack item;
        ItemMeta meta;
        String deactivated = "http://textures.minecraft.net/texture/adb0e5c6f99e66b74fa84277894b7e4b8f09b2452fb7e486d6ee42b045c26137";
        if (active) {
            item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.RED + "Everyone");
        } else {
            item = SkullCreator.itemFromUrl(deactivated);
            meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.DARK_GRAY + "Everyone");
        }
        meta.setLore(Arrays.asList(
                ChatColor.GRAY + "This backpack is accessible to",
                ChatColor.GRAY + "every player in the server."
        ));
        item.setItemMeta(meta);
        return item;
    }
    public static ItemStack friendsHead(boolean active) {
        ItemStack item;
        ItemMeta meta;
        String activeUrl = "http://textures.minecraft.net/texture/76cbae7246cc2c6e888587198c7959979666b4f5a4088f24e26e075f140ae6c3";
        String inactiveUrl = "http://textures.minecraft.net/texture/384b364df7d6df0e47f3fe457a775e99c72061eea11caa369dd10af2d569169";

        if (active) {
            item = SkullCreator.itemFromUrl(activeUrl);
            meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.YELLOW + "Friends");
        } else {
            item = SkullCreator.itemFromUrl(inactiveUrl);
            meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.DARK_GRAY + "Friends");
        }

        meta.setLore(Arrays.asList(
                ChatColor.GRAY + "This backpack is accessible to",
                ChatColor.GRAY + "all of your friends."
        ));
        item.setItemMeta(meta);
        return item;
    }
    public static ItemStack guildHead(boolean active) {
        ItemStack item;
        ItemMeta meta;
        String activeUrl = "http://textures.minecraft.net/texture/76fb54176ba80ab4b6424afbcd2174385431e93d36e57b6c319f96d460669f93";
        String inactiveUrl = "http://textures.minecraft.net/texture/a981867c6376e282a15d519e7968a923faaf82e225693c2b1e957bdb93b0d392";

        if (active) {
            item = SkullCreator.itemFromUrl(activeUrl);
            meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.AQUA + "Guild");
        } else {
            item = SkullCreator.itemFromUrl(inactiveUrl);
            meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.DARK_GRAY + "Guild");
        }

        meta.setLore(Arrays.asList(
                ChatColor.GRAY + "This backpack is accessible to",
                ChatColor.GRAY + "all of the guild members in your faction."
        ));
        item.setItemMeta(meta);
        return item;
    }
    public static ItemStack handpickedHead(boolean active) {
        ItemStack item;
        ItemMeta meta;
        String activeUrl = "http://textures.minecraft.net/texture/b24b639c490ed4ea28a0b092ae99f3dde5091e480ecbd1b9084165fc4f5a2d5";
        String inactiveUrl = "http://textures.minecraft.net/texture/7aeae06fb2c3c9cb8e8430ccde1583c56da63b7d68081c5cffb0857666de3fda";

        if (active) {
            item = SkullCreator.itemFromUrl(activeUrl);
            meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.DARK_PURPLE + "Handpicked");
        } else {
            item = SkullCreator.itemFromUrl(inactiveUrl);
            meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.DARK_GRAY + "Handpicked");
        }

        meta.setLore(Arrays.asList(
                ChatColor.GRAY + "This backpack is accessible to",
                ChatColor.GRAY + "a few handpicked people.",
                ChatColor.GRAY + "To define the handpicked",
                ChatColor.GRAY + "people left click this."
        ));
        item.setItemMeta(meta);
        return item;
    }
    public static ItemStack privateBackpack(boolean active) {
        ItemStack item;
        ItemMeta meta;
        String locked = "http://textures.minecraft.net/texture/4f472ad801e33e9fbf5422fb90aa35602875118c129afeadec5fa687c88f8289";
        String unlocked = "http://textures.minecraft.net/texture/22eb71262bd2dc3fdf34771a3fdd37b8d6fd104371acb701041d6eb54d8059f2";

        if (active) {
            item = SkullCreator.itemFromUrl(locked);
        } else {
            item = SkullCreator.itemFromUrl(unlocked);
        }
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_GRAY + "Private Backpack");
        meta.setLore(Arrays.asList(
                ChatColor.GRAY + "Private your backpack and no one",
                ChatColor.GRAY + "will be able to access it."
        ));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack friendsIcon() {
        String string = "http://textures.minecraft.net/texture/b24b639c490ed4ea28a0b092ae99f3dde5091e480ecbd1b9084165fc4f5a2d5";
        ItemStack item = SkullCreator.itemFromUrl(string);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "Handpicked List");
        meta.setLore(Arrays.asList(
                ChatColor.GRAY + "Define the handpicked people that you want",
                ChatColor.GRAY + "to be able to access this backpack"));
        item.setItemMeta(meta);
        return item;
    }
    public static ItemStack addPlayerToHandPickedList() {
        ItemStack item = new ItemStack(Material.SIGN);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_PURPLE +  "Add players");
        meta.setLore(Arrays.asList(
                ChatColor.GRAY + "A sign menu will pop-up for you.",
                ChatColor.GRAY + "There you will write the name of",
                ChatColor.GRAY + "the player you want to add to this",
                ChatColor.GRAY + "Backpack's whitelist "
        ));
        item.setItemMeta(meta);
        return item;
    }
    public static ItemStack previousPage(boolean active) {
        if(active) {
            String string = "http://textures.minecraft.net/texture/32ff8aaa4b2ec30bc5541d41c8782199baa25ae6d854cda651f1599e654cfc79";
            ItemStack item = SkullCreator.itemFromUrl(string);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.GREEN + "Previous page");
            item.setItemMeta(meta);
            return item;
        } else {
            String string = "http://textures.minecraft.net/texture/b76230a0ac52af11e4bc84009c6890a4029472f3947b4f465b5b5722881aacc7";
            ItemStack item = SkullCreator.itemFromUrl(string);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.WHITE + "Previous page");
            item.setItemMeta(meta);
            return item;
        }
    }
    public static ItemStack nextPage(boolean active) {
        if(active) {
            String string = "http://textures.minecraft.net/texture/aab95a8751aeaa3c671a8e90b83de76a0204f1be65752ac31be2f98feb64bf7f";
            ItemStack item = SkullCreator.itemFromUrl(string);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.GREEN + "Next page");
            item.setItemMeta(meta);
            return item;
        } else {
            String string = "http://textures.minecraft.net/texture/dbf8b6277cd36266283cb5a9e6943953c783e6ff7d6a2d59d15ad0697e91d43c";
            ItemStack item = SkullCreator.itemFromUrl(string);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.WHITE + "Next page");
            item.setItemMeta(meta);
            return item;
        }
    }
    public static ItemStack removeEveryone(ArrayList<ItemStack> checks) {
        ItemStack item;
        if(checks.isEmpty()) {
            item = new ItemStack(Material.MINECART);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.DARK_GRAY + "Remove all the players");
            meta.setLore(Collections.singletonList(ChatColor.GRAY + "You need to have at least 1 player to do that"));
            item.setItemMeta(meta);
        } else {
            item = new ItemStack(Material.HOPPER_MINECART);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.RED + "Remove all the players");
            meta.setLore(Arrays.asList(
                    ChatColor.GRAY + "By clicking this hopper and hitting",
                    ChatColor.GRAY + "yes on the confirmation menu you will",
                    ChatColor.GRAY + "remove everyone's access to this backpack"
            ));
            item.setItemMeta(meta);
        }
        return item;
    }

}

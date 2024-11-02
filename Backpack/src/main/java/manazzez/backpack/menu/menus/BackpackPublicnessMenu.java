package manazzez.backpack.menu.menus;

import manazzez.backpack.Main;
import manazzez.backpack.menu.Menu;
import manazzez.backpack.menu.PlayerMenuUtility;
import manazzez.backpack.utillities.BackpackObject;
import manazzez.backpack.utillities.ItemStacks;
import manazzez.backpack.utillities.PublicState;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class BackpackPublicnessMenu extends Menu {

    private BackpackObject backpackObject = null;

    public BackpackPublicnessMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public int getSlots() {
        return 36;
    }

    @Override
    public String getMenuName() {
        if (backpackObject != null) {
            return ChatColor.WHITE + "Edit backpack " + ChatColor.AQUA + backpackObject.getNumber() + ChatColor.WHITE + " publicness";
        }
        return ChatColor.WHITE + "Edit backpack publicness";
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        e.setCancelled(true);
        Player player = (Player) e.getWhoClicked();

        if (e.getCurrentItem().equals(ItemStacks.privateBackpack(false))) {
            updateBackpackState(player, PublicState.PRIVATE, false);
        } else if (e.getCurrentItem().equals(ItemStacks.privateBackpack(true))) {
            playSound(player, Sound.ANVIL_LAND);
        } else if (e.getCurrentItem().equals(ItemStacks.everyoneHead(false))) {
            updateBackpackState(player, PublicState.ANYONE, true);
        } else if (e.getCurrentItem().equals(ItemStacks.everyoneHead(true))) {
            playSound(player, Sound.ANVIL_LAND);
        } else if (e.getCurrentItem().equals(ItemStacks.friendsHead(false))) {
            updateBackpackState(player, PublicState.FRIENDS, true);
        } else if (e.getCurrentItem().equals(ItemStacks.friendsHead(true))) {
            playSound(player, Sound.ANVIL_LAND);
        } else if (e.getCurrentItem().equals(ItemStacks.guildHead(false))) {
            updateBackpackState(player, PublicState.GUILD, true);
        } else if (e.getCurrentItem().equals(ItemStacks.guildHead(true))) {
            playSound(player, Sound.ANVIL_LAND);
        } else if (e.getCurrentItem().equals(ItemStacks.handpickedHead(false))) {
            PickedPeopleMenu pickedPeopleMenu = new PickedPeopleMenu(Main.getPlayerMenuUtility(player));
            pickedPeopleMenu.open();
        } else if (e.getCurrentItem().equals(ItemStacks.handpickedHead(true))) {
            PickedPeopleMenu pickedPeopleMenu = new PickedPeopleMenu(Main.getPlayerMenuUtility(player));
            pickedPeopleMenu.open();
        } else if (e.getCurrentItem().equals(ItemStacks.toggle(false)) && e.getSlot() == 24) {
            updateBackpackState(player, PublicState.PRIVATE, false);
        } else if (e.getCurrentItem().equals(ItemStacks.toggle(true)) && e.getSlot() == 24) {
            playSound(player, Sound.ANVIL_LAND);
        } else if (e.getCurrentItem().equals(ItemStacks.toggle(false)) && e.getSlot() == 20) {
            updateBackpackState(player, PublicState.ANYONE, true);
        } else if (e.getCurrentItem().equals(ItemStacks.toggle(true)) && e.getSlot() == 20) {
            playSound(player, Sound.ANVIL_LAND);
        } else if (e.getCurrentItem().equals(ItemStacks.toggle(false)) && e.getSlot() == 21) {
            updateBackpackState(player, PublicState.FRIENDS, true);
        } else if (e.getCurrentItem().equals(ItemStacks.toggle(true)) && e.getSlot() == 21) {
            playSound(player, Sound.ANVIL_LAND);
        } else if (e.getCurrentItem().equals(ItemStacks.toggle(false)) && e.getSlot() == 22) {
            updateBackpackState(player, PublicState.GUILD, true);
        } else if (e.getCurrentItem().equals(ItemStacks.toggle(true)) && e.getSlot() == 22) {
            playSound(player, Sound.ANVIL_LAND);
        } else if (e.getCurrentItem().equals(ItemStacks.toggle(false)) && e.getSlot() == 23) {
            updateBackpackState(player, PublicState.HANDPICKED, true);
        } else if (e.getCurrentItem().equals(ItemStacks.toggle(true)) && e.getSlot() == 23) {
            playSound(player, Sound.ANVIL_LAND);
        } else if (e.getCurrentItem().equals(ItemStacks.returnArrow())) {
            BackpackEditMenu backpackEditMenu = new BackpackEditMenu(Main.getPlayerMenuUtility(player));
            backpackEditMenu.open();
        }
    }

    private void updateBackpackState(Player player, PublicState state, boolean isPublic) {
        backpackObject.setPublic(isPublic);
        backpackObject.setPublicState(state);
        String name = AdminViewerMenu.capitalizeFirstLetter(state.toString());
        player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1, 1);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&7You've changed the publicness of your backpack to &b" + name));
        setMenuItems();
        player.updateInventory();
    }

    private void playSound(Player player, Sound sound) {
        player.playSound(player.getLocation(), sound, 1, 1);
    }


    @Override
    public void setMenuItems() {
        inventory.setItem(31, ItemStacks.returnArrow());
        for (BackpackObject backpackObject : Main.getBackpacksMap().get(playerMenuUtility.getOwner().getName().toLowerCase())) {
            if (backpackObject.getNumber() == BackpacksMenu.clickedBackpack.get(playerMenuUtility.getOwner())) {
                this.backpackObject = backpackObject;
                break;
            }
        }

        PublicState state = backpackObject.getPublicState();
        switch (state) {
            case PRIVATE:
                setMenuItemsForPrivate();
                break;
            case ANYONE:
                setMenuItemsForAnyone();
                break;
            case FRIENDS:
                setMenuItemsForFriends();
                break;
            case GUILD:
                setMenuItemsForGuild();
                break;
            case HANDPICKED:
                setMenuItemsForHandpicked();
                break;
        }
    }

    private void setMenuItemsForPrivate() {
        setItem(11, ItemStacks.everyoneHead(false));
        setItem(20, ItemStacks.toggle(false));
        setItem(12, ItemStacks.friendsHead(false));
        setItem(21, ItemStacks.toggle(false));
        setItem(13, ItemStacks.guildHead(false));
        setItem(22, ItemStacks.toggle(false));
        setItem(14, ItemStacks.handpickedHead(false));
        setItem(23, ItemStacks.toggle(false));
        setItem(15, ItemStacks.privateBackpack(true));
        setItem(24, ItemStacks.toggle(true));
    }

    private void setMenuItemsForAnyone() {
        setItem(11, ItemStacks.everyoneHead(true));
        setItem(20, ItemStacks.toggle(true));
        setItem(12, ItemStacks.friendsHead(false));
        setItem(21, ItemStacks.toggle(false));
        setItem(13, ItemStacks.guildHead(false));
        setItem(22, ItemStacks.toggle(false));
        setItem(14, ItemStacks.handpickedHead(false));
        setItem(23, ItemStacks.toggle(false));
        setItem(15, ItemStacks.privateBackpack(false));
        setItem(24, ItemStacks.toggle(false));
    }

    private void setMenuItemsForFriends() {
        setItem(11, ItemStacks.everyoneHead(false));
        setItem(20, ItemStacks.toggle(false));
        setItem(12, ItemStacks.friendsHead(true));
        setItem(21, ItemStacks.toggle(true));
        setItem(13, ItemStacks.guildHead(false));
        setItem(22, ItemStacks.toggle(false));
        setItem(14, ItemStacks.handpickedHead(false));
        setItem(23, ItemStacks.toggle(false));
        setItem(15, ItemStacks.privateBackpack(false));
        setItem(24, ItemStacks.toggle(false));
    }

    private void setMenuItemsForGuild() {
        setItem(11, ItemStacks.everyoneHead(false));
        setItem(20, ItemStacks.toggle(false));
        setItem(12, ItemStacks.friendsHead(false));
        setItem(21, ItemStacks.toggle(false));
        setItem(13, ItemStacks.guildHead(true));
        setItem(22, ItemStacks.toggle(true));
        setItem(14, ItemStacks.handpickedHead(false));
        setItem(23, ItemStacks.toggle(false));
        setItem(15, ItemStacks.privateBackpack(false));
        setItem(24, ItemStacks.toggle(false));
    }

    private void setMenuItemsForHandpicked() {
        setItem(11, ItemStacks.everyoneHead(false));
        setItem(20, ItemStacks.toggle(false));
        setItem(12, ItemStacks.friendsHead(false));
        setItem(21, ItemStacks.toggle(false));
        setItem(13, ItemStacks.guildHead(false));
        setItem(22, ItemStacks.toggle(false));
        setItem(14, ItemStacks.handpickedHead(true));
        setItem(23, ItemStacks.toggle(true));
        setItem(15, ItemStacks.privateBackpack(false));
        setItem(24, ItemStacks.toggle(false));
    }

    private void setItem(int slot, ItemStack item) {
        inventory.setItem(slot, item);
    }
}

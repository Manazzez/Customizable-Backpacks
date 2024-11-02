package manazzez.backpack.menu;

import manazzez.backpack.utillities.ItemStacks;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class PaginatedMenu extends Menu{

    protected int page = 0;
    protected int index = 0;
    protected int maxItemsPerPage = 28;

    public PaginatedMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if(e.getCurrentItem().equals(ItemStacks.nextPage(false)) || e.getCurrentItem().equals(ItemStacks.previousPage(false))) {
            player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1, 1);
        } else if(e.getCurrentItem().equals(ItemStacks.nextPage(true))) {
            page = page + 1;
            open();
        } else if(e.getCurrentItem().equals(ItemStacks.previousPage(true))) {
            page = page - 1;
            open();
        }
    }

    public void addMenuBorders(int amount) {
        int[] panes = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53};
        for (int i : panes) {
            inventory.setItem(i, newPanes());
        }
        inventory.setItem(49, ItemStacks.returnArrow());

        if (page == 0) {
            inventory.setItem(48, ItemStacks.previousPage(false));
        } else if (page >= 1) {
            inventory.setItem(48, ItemStacks.previousPage(true));
        }

        index = (page + 1) * maxItemsPerPage;
        if (index < amount) {
            inventory.setItem(50, ItemStacks.nextPage(true));
        } else {
            inventory.setItem(50, ItemStacks.nextPage(false));
        }
    }
    public static ItemStack newPanes() {
        ItemStack item = ItemStacks.pane();
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("");
        meta.setLore(null);
        item.setItemMeta(meta);
        return item;
    }
}

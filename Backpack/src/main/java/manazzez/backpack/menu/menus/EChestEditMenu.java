package manazzez.backpack.menu.menus;

import manazzez.backpack.menu.Menu;
import manazzez.backpack.menu.PlayerMenuUtility;
import org.bukkit.ChatColor;
import org.bukkit.event.inventory.InventoryClickEvent;

public class EChestEditMenu extends Menu {

    public EChestEditMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public String getMenuName() {
        return ChatColor.WHITE + "Edit your enderchest";
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        e.setCancelled(true);
    }

    @Override
    public void setMenuItems() {

    }
}

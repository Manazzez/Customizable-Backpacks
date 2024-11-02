package manazzez.backpack.menu.signs;


import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import manazzez.backpack.Main;
import manazzez.backpack.menu.menus.BackpacksMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.awt.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class SignGUIAPI {
    private final SignCompleteHandler action;
    private PacketAdapter packetListener;
    private final List<String> lines;
    private LeaveListener listener;
    private final Plugin plugin;
    private final String name;
    private Sign sign;

    @Builder
    public SignGUIAPI(SignCompleteHandler action, List<String> withLines, String name, Plugin plugin) {
        this.lines = withLines;
        this.plugin = plugin;
        this.action = action;
        this.name = name;
    }

    public void open() {
        Player player = Bukkit.getPlayer(name);

        if (player == null) return;

        this.listener = new LeaveListener();

        int x_start = player.getLocation().getBlockX();

        int y_start = 255;

        int z_start = player.getLocation().getBlockZ();

        Material material = Material.getMaterial("WALL_SIGN");
        if (material == null)
            material = Material.WALL_SIGN;
        while (!player.getWorld().getBlockAt(x_start, y_start, z_start).getType().equals(Material.AIR) &&
                !player.getWorld().getBlockAt(x_start, y_start, z_start).getType().equals(material)) {
            y_start--;
            if (y_start == 1)
                return;
        }
        player.getWorld().getBlockAt(x_start, y_start, z_start).setType(material);

        this.sign = (Sign) player.getWorld().getBlockAt(x_start, y_start, z_start).getState();

        int i = 0;
        for (String line : lines) {

            line = line.replace("\\u0026", "&");
            line = ChatColor.translateAlternateColorCodes('&', line);
            this.sign.setLine(i, line); // Set the colored line
            i++;
        }

        this.sign.update(false, false);


        PacketContainer openSign = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.OPEN_SIGN_EDITOR);

        BlockPosition position = new BlockPosition(x_start, y_start, z_start);

        openSign.getBlockPositionModifier().write(0, position);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            try {
                ProtocolLibrary.getProtocolManager().sendServerPacket(player, openSign);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 3L);

        Bukkit.getPluginManager().registerEvents(this.listener, plugin);
        registerSignUpdateListener();
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    private class LeaveListener implements Listener {
        @EventHandler
        public void onLeave(PlayerQuitEvent e) {
            if (e.getPlayer().getName().equalsIgnoreCase(SignGUIAPI.this.name)) {
                ProtocolLibrary.getProtocolManager().removePacketListener(SignGUIAPI.this.packetListener);
                HandlerList.unregisterAll(this);
                SignGUIAPI.this.sign.getBlock().setType(Material.AIR);
            }
        }
    }

    private void registerSignUpdateListener() {
        final ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        this.packetListener = new PacketAdapter(plugin, PacketType.Play.Client.UPDATE_SIGN) {
            public void onPacketReceiving(PacketEvent event) {
                if (event.getPlayer().getName().equalsIgnoreCase(SignGUIAPI.this.name)) {
                    List<String> lines = Stream.of(0, 1, 2, 3)
                            .map(line -> ChatColor.translateAlternateColorCodes('&', getLine(event, line)))
                            .collect(Collectors.toList());

                    Bukkit.getScheduler().runTask(plugin, () -> {
                        manager.removePacketListener(this);
                        HandlerList.unregisterAll(SignGUIAPI.this.listener);

                        SignGUIAPI.this.sign.getBlock().setType(Material.AIR);

                        String backpackName = lines.get(0);
                        SignGUIAPI.this.action.onSignClose(new SignCompletedEvent(event.getPlayer(), lines));


                    });
                }
            }
        };
        manager.addPacketListener(this.packetListener);
    }


    private String getLine(PacketEvent event, int line) {
        return Bukkit.getVersion().contains("1.8") ?
                ((WrappedChatComponent[]) event.getPacket().getChatComponentArrays().read(0))[line].getJson().replaceAll("\"", "") :
                ((String[]) event.getPacket().getStringArrays().read(0))[line];
    }
}

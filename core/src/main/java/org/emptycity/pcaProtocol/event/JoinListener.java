package org.emptycity.pcaProtocol.event;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.emptycity.pcaProtocol.PcaProtocol;
import org.emptycity.pcaProtocol.network.PcaSyncProtocol;
import org.emptycity.pcaProtocol.packet.PacketSender;


/**
 * @Auther: Administrator
 * @Date: 2025/5/1 22:22:52
 * @Description:
 */
public class JoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // 玩家加入时发送数据包
        Bukkit.getScheduler().runTaskLater(PcaProtocol.plugin, () -> {
            sendCustomPacketOnJoin(event.getPlayer());
        }, 20);
    }

    private void sendCustomPacketOnJoin(Player player) {
        // 通知客户端启用pca同步
        PacketSender packetSender = new PacketSender();
        packetSender.sendCustomPacket(player, PcaSyncProtocol.ENABLE_PCA_SYNC_PROTOCOL, null);
        PcaProtocol.LOGGER.info("ENABLE_PCA_SYNC_PROTOCOL");
    }
}

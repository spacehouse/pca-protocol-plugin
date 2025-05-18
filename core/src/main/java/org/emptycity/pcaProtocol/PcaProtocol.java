package org.emptycity.pcaProtocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import io.netty.buffer.ByteBuf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.emptycity.pcaProtocol.event.JoinListener;
import org.emptycity.pcaProtocol.network.PcaSyncProtocol;
import org.emptycity.pcaProtocol.nms.NMSService;
import org.emptycity.pcaProtocol.util.BlockPosUtil;
import org.emptycity.pcaProtocol.util.PacketByteBufUtil;

public final class PcaProtocol extends JavaPlugin {
    public static final Logger LOGGER = LogManager.getLogger(PcaProtocol.class.getSimpleName());

    public static String version;
    public static Plugin plugin;

    private ProtocolManager protocolManager;
    private static PcaProtocol pca;

    @Override
    public void onEnable() {
        pca = this;
        NMSService.init();
        // Plugin startup logic
        version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        plugin = this;
        LOGGER.info("version: {}", version);
        LOGGER.info("Server: {}", Bukkit.getServer());

        PluginManager pluginManager = Bukkit.getPluginManager();
        if (!pluginManager.isPluginEnabled("ProtocolLib")) {
            LOGGER.error("ProtocolLib未安装");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        protocolManager = ProtocolLibrary.getProtocolManager();

        // 监听CUSTOM_PAYLOAD数据包
        protocolManager.addPacketListener(new PacketAdapter(
                this,
                ListenerPriority.NORMAL,
                PacketType.Play.Client.CUSTOM_PAYLOAD
        ) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                LOGGER.debug("CUSTOM_PAYLOAD数据包接收");
                PacketContainer packet = event.getPacket();
                String field0 = packet.getModifier().read(0).toString();
                Object field1 = packet.getModifier().read(1);
                LOGGER.debug("channel：{}", field0);
                // 获取原始字节缓冲区
                ByteBuf rawData = (ByteBuf) field1;
                rawData.resetReaderIndex();
                // 创建副本避免影响原始数据
                ByteBuf data = rawData.copy();
                if (PcaSyncProtocol.SYNC_ENTITY.equals(field0)) {
                    // 同步实体数据
//                    PacketByteBufUtil.log(data);
                    int entityId = PacketByteBufUtil.readVarInt(data);
                    LOGGER.debug("entityId：{}",entityId);
                    PcaSyncProtocol.syncEntityHandler(entityId, event.getPlayer());
                } else if (PcaSyncProtocol.SYNC_BLOCK_ENTITY.equals(field0)) {
                    // 同步方块实体数据
//                    PacketByteBufUtil.log(data);
                    long blockPositionLong = PacketByteBufUtil.readLong(data);
                    LOGGER.debug("pca数据包内容：{}",blockPositionLong);
                    BlockPosition blockPos= BlockPosUtil.toBlockPos(blockPositionLong);
                    World world = event.getPlayer().getWorld();
                    Location location = blockPos.toLocation(world);
                    PcaSyncProtocol.syncBlockEntityHandler(location, event.getPlayer());
                } else {
                    String str = PacketByteBufUtil.readString(data);
                    LOGGER.debug("其他数据包内容：{}",str);
                }
            }
        });

        registerEvent(pluginManager);

        LOGGER.info("PcaProtocol enabled");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        LOGGER.info("PcaProtocol disabled");
    }

    private void registerEvent(PluginManager pluginManager) {
        pluginManager.registerEvents(new JoinListener(), this);
    }

    public static PcaProtocol getInstance() {
        return pca;
    }

}

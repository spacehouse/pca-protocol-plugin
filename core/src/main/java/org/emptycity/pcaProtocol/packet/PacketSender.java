package org.emptycity.pcaProtocol.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.reflect.accessors.FieldAccessor;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.bukkit.entity.Player;
import org.emptycity.pcaProtocol.PcaProtocol;
import org.emptycity.pcaProtocol.nms.NMSService;

import java.util.List;

/**
 * @Auther: Administrator
 * @Date: 2025/5/2 03:17:17
 * @Description:
 */
public class PacketSender {
    // 获取 ProtocolLib 的协议管理器
    private static final ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

    /**
     * 向指定玩家发送自定义数据包
     * @param player  目标玩家
     * @param channel 通道名（如 "mymod:channel"）
     * @param byteByf  数据内容（ByteBuf）
     */
    public void sendCustomPacket(Player player, String channel, ByteBuf byteByf) {
        // 创建数据包容器
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.CUSTOM_PAYLOAD);
        // 动态适配字段索引
        int channelIndex = -1, dataIndex = -1;
        List<FieldAccessor> fields = packet.getModifier().getFields();
        for (int i = 0; i < fields.size(); i++) {
            String fieldName = fields.get(i).getField().getType().toString().toLowerCase();
            if (fieldName.contains("channel") || fieldName.contains("identifier") || fieldName.contains("key")) {
                channelIndex = i;
            } else if (fieldName.contains("data")) {
                dataIndex = i;
            }
        }

        if (channelIndex == -1 || dataIndex == -1) {
            throw new IllegalStateException("the packet fields do not match");
        }

        // 写入通道名
        if (packet.getModifier().getField(channelIndex).getType() == String.class) {
            packet.getStrings().write(channelIndex, channel);
        } else {
            Object resourceLocation = NMSService.getResourceLocation().getInstance(channel);
            packet.getModifier().write(channelIndex, resourceLocation);
        }

        // 设置数据体（动态处理 PacketDataSerializer 或 byte[]）
        Class<?> dataFieldType = packet.getModifier().getField(dataIndex).getType();
        if (dataFieldType == byte[].class) {
            PcaProtocol.LOGGER.debug("旧版数据包");
            // 旧版本直接写入 byte[]
            ByteBuf buf = Unpooled.buffer();
            if (byteByf != null) {
                buf = byteByf;
            }
            byte[] dataBytes = new byte[buf.readableBytes()];
            buf.readBytes(dataBytes);
            packet.getByteArrays().write(dataIndex, dataBytes);
        } else if (dataFieldType.toString().contains("PacketDataSerializer")) {
            // 新版本写入 PacketDataSerializer
            ByteBuf buf = Unpooled.buffer();
            if (byteByf != null) {
                buf = byteByf;
            }
            Object friendlyByteBuf = NMSService.getFriendlyByteBuf().getInstance(buf);
            packet.getModifier().write(dataIndex, friendlyByteBuf);
        }

        // 5. 发送数据包
        try {
            protocolManager.sendServerPacket(player, packet);
            PcaProtocol.LOGGER.debug("发送自定义数据包成功！player={}", player.getName());
        } catch (Exception e) {
            PcaProtocol.LOGGER.error("发送自定义数据包失败！player={}", player.getName(), e);
        }
    }
}

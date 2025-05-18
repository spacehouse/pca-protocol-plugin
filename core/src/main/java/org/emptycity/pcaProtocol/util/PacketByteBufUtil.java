package org.emptycity.pcaProtocol.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.emptycity.pcaProtocol.PcaProtocol;
import org.emptycity.pcaProtocol.nms.NMSService;

import java.nio.charset.StandardCharsets;

import static io.netty.buffer.ByteBufUtil.appendPrettyHexDump;
import static io.netty.util.internal.StringUtil.NEWLINE;

/**
 * @Auther: Administrator
 * @Date: 2025/5/11 18:25:24
 * @Description:
 */
public class PacketByteBufUtil {

    public static int readVarInt(ByteBuf buf) {
        buf.resetReaderIndex();
        return buf.readInt();
    }

    public static long readLong(ByteBuf buf) {
        buf.resetReaderIndex();
        return buf.readLong();
    }

    public static String readString(ByteBuf buf) {
        int length = buf.readableBytes();
        byte[] bytes = new byte[length];
        buf.resetReaderIndex();
        buf.readBytes(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static void log(ByteBuf buffer) {
        buffer.resetReaderIndex();
        int length = buffer.readableBytes();
        int rows = length / 16 + (length % 15 == 0 ? 0 : 1) + 4;
        StringBuilder buf = new StringBuilder(rows * 80 * 2)
                .append("read index:").append(buffer.readerIndex())
                .append(" write index:").append(buffer.writerIndex())
                .append(" capacity:").append(buffer.capacity())
                .append(NEWLINE);
        appendPrettyHexDump(buf, buffer);
        System.out.println(buf);
    }

    public static void writeString(ByteBuf buf, String string, int maxLength) {
        // 检查字符长度是否超过最大限制
        if (string.length() > maxLength) {
            throw new RuntimeException("String too long (was " + string.length() + " characters, max " + maxLength + ")");
        }
        int i = io.netty.buffer.ByteBufUtil.utf8MaxBytes(string);
        ByteBuf byteBuf = buf.alloc().buffer(i);

        try {
            int j = io.netty.buffer.ByteBufUtil.writeUtf8(byteBuf, string);
            int k = io.netty.buffer.ByteBufUtil.utf8MaxBytes(maxLength);
            if (j > k) {
                throw new RuntimeException("String too big (was " + j + " bytes encoded, max " + k + ")");
            }

            // 写入VarInt长度和字节数据
            writeByte(buf, j);
            buf.writeBytes(byteBuf);
        } finally {
            byteBuf.release();
        }
    }

    public static void writeByte(ByteBuf buf, int i) {
        while((i & -128) != 0) {
            buf.writeByte(i & 127 | 128);
            i >>>= 7;
        }
        buf.writeByte(i);
    }

    public static void writePos(ByteBuf buf, Block block) {
        if (block == null) {
            PcaProtocol.LOGGER.debug("方块为空");
            return;
        }
        Location location = block.getLocation();
        long aLong = BlockPosUtil.toLong(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        buf.writeLong(aLong);
    }

    public static void writeNbt(Object compoundTag, ByteBuf byteBuf) {
        try {
            if (compoundTag == null) {
                PcaProtocol.LOGGER.debug("NBT数据为空");
                return;
            }
            // nbt写入
            byte nbtType = NMSService.getCompoundTag().getId(compoundTag);
            ByteBufOutputStream output = new ByteBufOutputStream(byteBuf);
            output.writeByte(nbtType);
            NMSService.getCompoundTag().write(compoundTag, output);
        } catch (Exception e) {
            PcaProtocol.LOGGER.error("nbt写入失败！", e);
        }
    }

}

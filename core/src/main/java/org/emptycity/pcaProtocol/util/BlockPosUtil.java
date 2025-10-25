package org.emptycity.pcaProtocol.util;

import com.comphenix.protocol.wrappers.BlockPosition;


/**
 * @Auther: Administrator
 * @Date: 2025/5/6 23:29:05
 * @Description:
 */
public class BlockPosUtil {
    // 从 long 编码中提取 x 坐标
    public static int getX(long encoded) {
        return (int) (encoded >> 38);
    }

    // 从 long 编码中提取 y 坐标
    public static int getY(long encoded) {
        return (int) ((encoded << 52) >> 52);
    }

    // 从 long 编码中提取 z 坐标
    public static int getZ(long encoded) {
        return (int) ((encoded << 26) >> 38);
    }

    // 将 long 转换为 Bukkit 的 Location 对象
    public static BlockPosition toBlockPos(long encoded) {
        int x = getX(encoded);
        int y = getY(encoded);
        int z = getZ(encoded);
        return new BlockPosition(x, y, z);
    }

    public static long toLong(int x, int y, int z) {
        return (((long) x & 0x3FFFFFFL) << 38) | (((long) z & 0x3FFFFFFL) << 12) | (((long) y) & 0xFFFL);
    }
}

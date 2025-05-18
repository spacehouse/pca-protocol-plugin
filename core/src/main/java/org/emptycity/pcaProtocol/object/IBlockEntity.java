package org.emptycity.pcaProtocol.object;

/**
 * @Auther: Administrator
 * @Date: 2025/5/17 06:55:49
 * @Description:
 */
public interface IBlockEntity {
    Object getCompoundTag(Object blockEntity);

    Object getWorld(Object blockEntity);
}

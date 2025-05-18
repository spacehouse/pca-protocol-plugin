package org.emptycity.pcaProtocol.object;

import org.bukkit.World;

/**
 * @Auther: Administrator
 * @Date: 2025/5/17 05:54:14
 * @Description:
 */
public interface IWorld {
    Object getInstance(World world);

    Object getEntity(World world, int entityId);

    String getDimId(Object world);

    Object getBlockEntity(World world, int x, int y, int z);

}

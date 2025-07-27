package org.emptycity.pcaProtocol.nms.v1_20_3.object;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.emptycity.pcaProtocol.PcaProtocol;
import org.emptycity.pcaProtocol.object.IWorld;

/**
 * @Auther: Administrator
 * @Date: 2025/5/17 07:15:19
 * @Description:
 */
public class WorldImpl implements IWorld {
    @Override
    public Level getInstance(World world) {
        try {
            Class<?> craftWorldClass = Class.forName("org.bukkit.craftbukkit." + PcaProtocol.version + ".CraftWorld");
            Object craftWorld = craftWorldClass.cast(world);
            return (Level) craftWorld.getClass().getDeclaredMethod("getHandle").invoke(craftWorld);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Get World instance error", e);
        }
    }

    @Override
    public Entity getEntity(World world, int entityId) {
        Level level = getInstance(world);
        return level.getEntity(entityId);
    }

    @Override
    public String getDimId(Object world) {
        return ((Level) world).dimension().location().toString();
    }

    @Override
    public BlockEntity getBlockEntity(World world, int x, int y, int z) {
        Level level = getInstance(world);
        return level.getBlockEntity(new BlockPos(x, y, z));
    }
}

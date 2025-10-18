package org.emptycity.pcaProtocol.nms.v1_21_7.object;

import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.emptycity.pcaProtocol.object.IBlockEntity;

/**
 * @Auther: Administrator
 * @Date: 2025/5/17 07:15:59
 * @Description:
 */
public class BlockEntityImpl implements IBlockEntity {

    public BlockEntity getInstance(Object blockEntity) {
        return (BlockEntity) blockEntity;
    }
    @Override
    public CompoundTag getCompoundTag(Object blockEntity) {
        BlockEntity instance = getInstance(blockEntity);
        RegistryAccess registryAccess = instance.getLevel().registryAccess();
        return getInstance(blockEntity).saveWithoutMetadata(registryAccess);
    }

    @Override
    public Level getWorld(Object blockEntity) {
        return getInstance(blockEntity).getLevel();
    }
}

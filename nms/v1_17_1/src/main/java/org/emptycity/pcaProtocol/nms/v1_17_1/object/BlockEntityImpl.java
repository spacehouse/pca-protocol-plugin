package org.emptycity.pcaProtocol.nms.v1_17_1.object;

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
        return getInstance(blockEntity).save(new CompoundTag());
    }

    @Override
    public Level getWorld(Object blockEntity) {
        return getInstance(blockEntity).getLevel();
    }
}

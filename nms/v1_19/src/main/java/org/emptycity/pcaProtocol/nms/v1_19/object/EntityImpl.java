package org.emptycity.pcaProtocol.nms.v1_19.object;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.emptycity.pcaProtocol.object.IEntity;

/**
 * @Auther: Administrator
 * @Date: 2025/5/17 07:15:39
 * @Description:
 */
public class EntityImpl implements IEntity {
    @Override
    public Entity getInstance(Object entity) {
        return (Entity) entity;
    }

    @Override
    public Level getEntityWorld(Object entity) {
        return getInstance(entity).getCommandSenderWorld();
    }

    @Override
    public CompoundTag getCompoundTag(Object entity) {
        return getInstance(entity).saveWithoutId(new CompoundTag());
    }
}

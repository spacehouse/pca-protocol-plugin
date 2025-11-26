package org.emptycity.pcaProtocol.nms.v1_21_9.object;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.TagValueOutput;
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
        return getInstance(entity).level();
    }

    @Override
    public CompoundTag getCompoundTag(Object entity) {
        Entity instance = getInstance(entity);
        TagValueOutput tagValueOutput = TagValueOutput.createWithoutContext(ProblemReporter.DISCARDING);
        instance.saveWithoutId(tagValueOutput);
        return tagValueOutput.buildResult();
    }
}

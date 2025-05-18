package org.emptycity.pcaProtocol.object;

/**
 * @Auther: Administrator
 * @Date: 2025/5/17 06:27:11
 * @Description:
 */
public interface IEntity {
    Object getInstance(Object entity);

    Object getEntityWorld(Object entity);

    Object getCompoundTag(Object entity);
}

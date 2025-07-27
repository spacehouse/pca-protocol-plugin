package org.emptycity.pcaProtocol.object;

/**
 * @Auther: Administrator
 * @Date: 2025/7/27 03:57:59
 * @Description:
 */
public interface IDiscardedPayload {
    Object getInstance(Object payload);

    String getId(Object payload);

    byte[] getData(Object payload);

    Object constructor(String key, byte[] data);
}

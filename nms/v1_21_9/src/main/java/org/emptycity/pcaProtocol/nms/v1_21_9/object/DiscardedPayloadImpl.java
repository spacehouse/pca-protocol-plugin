package org.emptycity.pcaProtocol.nms.v1_21_9.object;

import net.minecraft.network.protocol.common.custom.DiscardedPayload;
import net.minecraft.resources.ResourceLocation;
import org.emptycity.pcaProtocol.object.IDiscardedPayload;

/**
 * @Auther: Administrator
 * @Date: 2025/7/27 03:57:30
 * @Description:
 */
public class DiscardedPayloadImpl implements IDiscardedPayload {
    @Override
    public DiscardedPayload getInstance(Object payload) {
        return (DiscardedPayload) payload;
    }

    @Override
    public String getId(Object payload) {
        return getInstance(payload).id().toString();
    }

    @Override
    public byte[] getData(Object payload) {
        return getInstance(payload).data();
    }

    @Override
    public DiscardedPayload constructor(String key, byte[] data) {
        return new DiscardedPayload(ResourceLocation.parse(key), data);
    }
}

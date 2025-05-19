package org.emptycity.pcaProtocol.nms.v1_20_3.object;

import org.emptycity.pcaProtocol.object.IResourceLocation;
import net.minecraft.resources.ResourceLocation;

/**
 * @Auther: Administrator
 * @Date: 2025/5/17 04:24:50
 * @Description:
 */
public class ResourceLocationImpl implements IResourceLocation {
    @Override
    public ResourceLocation getInstance(String channel) {
        return new ResourceLocation(channel);
    }
}

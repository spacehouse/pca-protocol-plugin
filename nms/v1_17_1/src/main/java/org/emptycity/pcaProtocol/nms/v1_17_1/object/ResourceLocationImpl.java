package org.emptycity.pcaProtocol.nms.v1_17_1.object;

import net.minecraft.resources.ResourceLocation;
import org.emptycity.pcaProtocol.object.IResourceLocation;

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

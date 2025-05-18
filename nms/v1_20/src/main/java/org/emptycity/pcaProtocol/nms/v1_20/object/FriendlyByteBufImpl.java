package org.emptycity.pcaProtocol.nms.v1_20.object;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import org.emptycity.pcaProtocol.object.IFriendlyByteBuf;

/**
 * @Auther: Administrator
 * @Date: 2025/5/17 04:58:03
 * @Description:
 */
public class FriendlyByteBufImpl implements IFriendlyByteBuf {

    @Override
    public FriendlyByteBuf getInstance(ByteBuf byteBuf) {
        return new FriendlyByteBuf(byteBuf);
    }
}

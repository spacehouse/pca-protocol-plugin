package org.emptycity.pcaProtocol.object;

import io.netty.buffer.ByteBuf;

/**
 * @Auther: Administrator
 * @Date: 2025/5/17 04:20:28
 * @Description:
 */
public interface IFriendlyByteBuf {
    Object getInstance(ByteBuf byteBuf);
}

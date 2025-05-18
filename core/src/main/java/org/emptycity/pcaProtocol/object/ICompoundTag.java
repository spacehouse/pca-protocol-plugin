package org.emptycity.pcaProtocol.object;

import java.io.DataOutput;
import java.io.IOException;

/**
 * @Auther: Administrator
 * @Date: 2025/5/17 05:08:06
 * @Description:
 */
public interface ICompoundTag {

    byte getId(Object compoundTag);

    void write(Object compoundTag, DataOutput output) throws IOException;
}

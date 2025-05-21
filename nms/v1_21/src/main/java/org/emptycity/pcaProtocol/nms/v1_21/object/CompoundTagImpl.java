package org.emptycity.pcaProtocol.nms.v1_21.object;

import net.minecraft.nbt.CompoundTag;
import org.emptycity.pcaProtocol.object.ICompoundTag;

import java.io.DataOutput;
import java.io.IOException;

/**
 * @Auther: Administrator
 * @Date: 2025/5/17 05:19:55
 * @Description:
 */
public class CompoundTagImpl implements ICompoundTag {
    @Override
    public byte getId(Object compoundTag) {
        return ((CompoundTag) compoundTag).getId();
    }

    @Override
    public void write(Object compoundTag, DataOutput output) throws IOException {
        ((CompoundTag) compoundTag).write(output);
    }
}

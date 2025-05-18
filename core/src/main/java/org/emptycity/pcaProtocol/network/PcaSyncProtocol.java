package org.emptycity.pcaProtocol.network;

import io.netty.buffer.*;
import org.bukkit.block.Block;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Chest;
import org.bukkit.entity.Player;
import org.emptycity.pcaProtocol.PcaProtocol;
import org.emptycity.pcaProtocol.nms.NMSService;
import org.emptycity.pcaProtocol.object.IEntity;
import org.emptycity.pcaProtocol.object.IWorld;
import org.emptycity.pcaProtocol.packet.PacketSender;
import org.emptycity.pcaProtocol.util.PacketByteBufUtil;

/**
 * @Auther: Administrator
 * @Date: 2025/5/1 19:10:04
 * @Description:
 */
public class PcaSyncProtocol {

    private static final String NAMESPACE = "pca";
    // recv
    public static final String SYNC_BLOCK_ENTITY = id("sync_block_entity");
    public static final String SYNC_ENTITY = id("sync_entity");
    public static final String CANCEL_SYNC_REQUEST_BLOCK_ENTITY = id("cancel_sync_block_entity");
    public static final String CANCEL_SYNC_ENTITY = id("cancel_sync_entity");
    // send
    public static final String ENABLE_PCA_SYNC_PROTOCOL = id("enable_pca_sync_protocol");
    public static final String DISABLE_PCA_SYNC_PROTOCOL = id("disable_pca_sync_protocol");
    public static final String UPDATE_ENTITY = id("update_entity");
    public static final String UPDATE_BLOCK_ENTITY = id("update_block_entity");

    // ======================================= Protocol Init =======================================

    public static String id(String name) {
        return NAMESPACE + ":" + name;
    }


    public static void syncBlockEntityHandler(Location blockLocation, Player player) {
        Bukkit.getScheduler().runTask(PcaProtocol.plugin, () -> {
            try {
                Block block = blockLocation.getBlock();
                Block adjacentChestBlock = null;
                if (block.getType() == Material.CHEST) {
                    Chest chest = (Chest) block.getBlockData();
                    PcaProtocol.LOGGER.debug("chest数据：" + chest);
                    // 大箱子处理
                    if (chest.getType() != Chest.Type.SINGLE) {
                        adjacentChestBlock = getAdjacentChestBlock(block);
                        PcaProtocol.LOGGER.debug("adjacentChestBlock数据：" + adjacentChestBlock);
                    }
                } else if (block.getType() == Material.BARREL) {
                    // carpet的大木桶，暂不处理
                }
                if (adjacentChestBlock != null) {
                    updateBlockEntity(adjacentChestBlock, player);
                }
                if (block != null) {
                    updateBlockEntity(block, player);
                }
            } catch (Exception e) {
                PcaProtocol.LOGGER.error("同步方块实体数据异常！error={}", e.getMessage());
            }
        });

    }

    public static void syncEntityHandler(int entityId, Player player) {
        Bukkit.getScheduler().runTask(PcaProtocol.plugin, () -> {
            // 获取实体
            try {
                IWorld world = NMSService.getWorld();
                Object entity = world.getEntity(player.getWorld(), entityId);
                if (entity != null) {
                    updateEntity(entityId, entity, player);
                }
            } catch (Exception e) {
                PcaProtocol.LOGGER.error("同步实体数据异常！error={}", e.getMessage());
            }
        });
    }

    public static void updateBlockEntity(Block block, Player player) {
        ByteBuf buf = Unpooled.buffer();
        try {
            Object blockEntity = NMSService.getWorld().getBlockEntity(player.getWorld(), block.getX(), block.getY(), block.getZ());
            Object world = NMSService.getBlockEntity().getWorld(blockEntity);
            String dimId = NMSService.getWorld().getDimId(world);
            if (dimId == null || dimId.isEmpty()) {
                PcaProtocol.LOGGER.debug("无法获取实体的维度！");
                buf.release();
                return;
            }
            // 世界维度写入
            PacketByteBufUtil.writeString(buf, dimId, 32767);
            // 方块位置写入
            PacketByteBufUtil.writePos(buf, block);
            // 获取方块数据NBT并写入
            Object blockEntityNBT = NMSService.getBlockEntity().getCompoundTag(blockEntity);
            if (blockEntityNBT == null) {
                PcaProtocol.LOGGER.error("NBT为空，不发包");
                buf.release();
                return;
            }
            PacketByteBufUtil.writeNbt(blockEntityNBT, buf);
            // 发送数据包
            PacketSender packetSender = new PacketSender();
            packetSender.sendCustomPacket(player, UPDATE_BLOCK_ENTITY, buf);
        } catch (Exception e) {
            buf.release();
            PcaProtocol.LOGGER.error("更新方块实体数据异常！error={}", e.getMessage());
        }
    }

    public static void updateEntity(int entityId, Object entity, Player player) {
        ByteBuf buf = Unpooled.buffer();
        try {
            IEntity nmsEntity = NMSService.getEntity();
            Object entityWorld = nmsEntity.getEntityWorld(entity);
            String dimId = NMSService.getWorld().getDimId(entityWorld);
            if (dimId == null || dimId.isEmpty()) {
                PcaProtocol.LOGGER.debug("无法获取实体的维度！");
                buf.release();
                return;
            }
            // 世界维度写入
            PacketByteBufUtil.writeString(buf, dimId, 32767);
            // 实体ID写入
            buf.writeInt(entityId);
            // 获取实体NBT并写入
            Object entityNBT = nmsEntity.getCompoundTag(entity);
            if (entityNBT == null) {
                PcaProtocol.LOGGER.error("NBT为空，不发包");
                buf.release();
                return;
            }
            PacketByteBufUtil.writeNbt(entityNBT, buf);
            // 发送数据包
            PacketSender packetSender = new PacketSender();
            packetSender.sendCustomPacket(player, UPDATE_ENTITY, buf);
        } catch (Exception e) {
            buf.release();
            PcaProtocol.LOGGER.error("更新实体数据异常！error={}", e.getMessage());
        }
    }

    public static Block getAdjacentChestBlock(Block chestBlock) {
        Chest chest = (Chest) chestBlock.getBlockData();
        BlockFace facing = chest.getFacing();
        Chest.Type type = chest.getType();

        // 计算偏移量
        int dx = 0, dz = 0;
        switch (facing) {
            case NORTH:
                dx = type == Chest.Type.LEFT ? 1 : -1;
                break;
            case SOUTH:
                dx = type == Chest.Type.LEFT ? -1 : 1;
                break;
            case WEST:
                dz = type == Chest.Type.LEFT ? -1 : 1;
                break;
            case EAST:
                dz = type == Chest.Type.LEFT ? 1 : -1;
                break;
            default:
                break;
        }

        // 获取相邻方块
        return chestBlock.getRelative(dx, 0, dz);
    }

}

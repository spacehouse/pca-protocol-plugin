package org.emptycity.pcaProtocol.nms;

import org.bukkit.Bukkit;
import org.emptycity.pcaProtocol.PcaProtocol;
import org.emptycity.pcaProtocol.object.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: Administrator
 * @Date: 2025/5/10 09:29:27
 * @Description:
 */
public class NMSService {
    private static IResourceLocation resourceLocation;
    private static IFriendlyByteBuf friendlyByteBuf;
    private static ICompoundTag compoundTag;
    private static IWorld world;
    private static IEntity entity;
    private static IBlockEntity blockEntity;
    private static IDiscardedPayload discardedPayload;


    public static IResourceLocation getResourceLocation() {
        return resourceLocation;
    }

    public static IFriendlyByteBuf getFriendlyByteBuf() {
        return friendlyByteBuf;
    }

    public static ICompoundTag getCompoundTag() {
        return compoundTag;
    }

    public static IWorld getWorld() {
        return world;
    }

    public static IEntity getEntity() {
        return entity;
    }

    public static IBlockEntity getBlockEntity() {
        return blockEntity;
    }

    public static IDiscardedPayload getDiscardedPayload() {
        return discardedPayload;
    }
    /**
     * 根据 NMS 版本，反射读取各个实现类
     */
    private static void load() {
        resourceLocation = (IResourceLocation) getPackageObjectInstance("object.ResourceLocationImpl");
        friendlyByteBuf = (IFriendlyByteBuf) getPackageObjectInstance("object.FriendlyByteBufImpl");
        compoundTag = (ICompoundTag) getPackageObjectInstance("object.CompoundTagImpl");
        world = (IWorld) getPackageObjectInstance("object.WorldImpl");
        entity = (IEntity) getPackageObjectInstance("object.EntityImpl");
        blockEntity = (IBlockEntity) getPackageObjectInstance("object.BlockEntityImpl");
        discardedPayload = (IDiscardedPayload) getPackageObjectInstance("object.DiscardedPayloadImpl");
    }

    private static final String LATEST_VERSION = "v1_21_8";
    private static final Map<String, String> VERSION_MAPPING = new HashMap<String, String>() {{
        put("1.17", "v1_17_1");
        put("1.17.1", "v1_17_1");
        put("1.18", "v1_18");
        put("1.18.1", "v1_18");
        put("1.18.2", "v1_18");
        put("1.19", "v1_19");
        put("1.19.1", "v1_19_1");
        put("1.19.2", "v1_19_1");
        put("1.19.3", "v1_19_3");
        put("1.19.4", "v1_19_4");
        put("1.20", "v1_20");
        put("1.20.1", "v1_20");
        put("1.20.2", "v1_20_2");
        put("1.20.3", "v1_20_3");
        put("1.20.4", "v1_20_3");
        put("1.20.5", "v1_20_6");
        put("1.20.6", "v1_20_6");
        put("1.21", "v1_21");
        put("1.21.1", "v1_21");
        put("1.21.2", "v1_21_3");
        put("1.21.3", "v1_21_3");
        put("1.21.4", "v1_21_4");
        put("1.21.5", "v1_21_5");
        put("1.21.6", "v1_21_6");
        put("1.21.7", "v1_21_7");
        put("1.21.8", "v1_21_7");
    }};

    private static String serverVersion;
    private static String packagePath;
    private static Boolean available;

    @SuppressWarnings("UnusedReturnValue")
    public static boolean init() {
        String rawServerVersion = Bukkit.getServer().getBukkitVersion();
        serverVersion = rawServerVersion.substring(0, rawServerVersion.indexOf('-'));
        packagePath = PcaProtocol.class.getPackage().getName() + ".nms." + getPackageVersion();
        available = hasPackageClass("object.WorldImpl");
        if(available) {
            load();
            return true;
        } else {
            packagePath = PcaProtocol.class.getPackage().getName() + ".mcv." + LATEST_VERSION;
            available = hasPackageClass("object.ResourceLocationImpl");
            if(available) {
                load();
                return true;
            }
        }
        return false;
    }

    public static Object getPackageObjectInstance(String className, Object... parameters) {
        try {
            Class<?> mcvPackageClass = Class.forName(packagePath + "." + className);
            if(parameters.length == 0) return mcvPackageClass.getConstructor().newInstance();
            Class<?>[] parameterTypes = Arrays.stream(parameters).map(Object::getClass).toArray(Class<?>[]::new);
            return mcvPackageClass.getConstructor(parameterTypes).newInstance(parameters);
        } catch(Throwable e) {
            PcaProtocol.LOGGER.error("Could not get package object with class name '" + className + "'!", e);
        }
        return null;
    }

    public static boolean hasPackageClass(String className) {
        try {
            Class.forName(packagePath + "." + className);
            return true;
        } catch(Throwable e) {
            PcaProtocol.LOGGER.error("Could not get package class with class name '" + className + "'!", e);
        }
        return false;
    }

    private static String getPackageVersion() {
        String packageVersion = "v" + serverVersion.replace(".", "_");
        return VERSION_MAPPING.getOrDefault(serverVersion, packageVersion);
    }

    public static String getPackagePath() {
        return packagePath;
    }

    public static Boolean isAvailable() {
        return available;
    }
}

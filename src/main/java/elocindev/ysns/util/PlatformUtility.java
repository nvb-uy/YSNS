package elocindev.ysns.util;

public class PlatformUtility {
    public static boolean isModLoaded(String modid) {
        //#if FABRIC==1
        return net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded(modid);
        //#else
        //$$ return net.minecraftforge.fml.ModList.get().isLoaded(modid);
        //#endif
    }
}

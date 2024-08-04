package elocindev.ysns.event;

import elocindev.ysns.api.YSNSAPI;
import net.minecraft.world.entity.LivingEntity;

//#if FORGE==1
//$$ import elocindev.ysns.YSNS;
//$$ import net.minecraft.server.level.ServerPlayer;
//$$ import net.minecraft.world.entity.player.Player;
//$$ import net.minecraftforge.event.entity.EntityJoinLevelEvent;
//$$ import net.minecraft.world.level.Level;

//#if MC>=12001
//$$ import net.minecraftforge.event.entity.living.MobSpawnEvent;
//#endif

//$$ import net.minecraftforge.eventbus.api.SubscribeEvent;
//$$ import net.minecraftforge.eventbus.api.Event.Result;
//$$ import net.minecraftforge.fml.common.Mod;

//$$@Mod.EventBusSubscriber(modid = YSNS.MODID)
//#else

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;

//#endif
public class SpawnEventHandler {
    //#if FORGE==1
    //$$ @SubscribeEvent
    //$$ public static void fallbackSpawnEvent(EntityJoinLevelEvent event) {
    //$$    if (event.getLevel().isClientSide()) return;
    //$$    if (event.getEntity() instanceof LivingEntity && YSNSAPI.isEntityDisabled(event.getEntity(), event.getEntity().getLevel())) {
    //$$        event.setCanceled(true);
    //$$    }
    //$$}

    //#if MC>=12001
    //$$ @SubscribeEvent
    //$$ public static void fallbackFinalizeSpawnEvent(MobSpawnEvent.FinalizeSpawn event) {
    //$$    if (event.getLevel().isClientSide()) return;
    //$$    if (event.getEntity() instanceof LivingEntity && YSNSAPI.isEntityDisabled(event.getEntity(), event.getEntity().level())) {
    //$$        event.setSpawnCancelled(true);
    //$$    }
    //$$}
    //#endif

    //#endif
    
    //#if FABRIC==1
    public static void register() {
        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (YSNSAPI.isEntityDisabled(entity, world)) {
                if (entity instanceof LivingEntity livingentity) {
                    livingentity.discard();
                }
            }
        });
    }
    //#endif
}

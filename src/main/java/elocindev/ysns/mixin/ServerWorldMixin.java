package elocindev.ysns.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import elocindev.ysns.api.YSNSAPI;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

@Mixin(ServerLevel.class)
public class ServerWorldMixin {
    @Inject(method = "addEntity", at = @At("HEAD"), cancellable = true)
    private void ysns$removeDisabledEntitiesOnSpawn(Entity entity, CallbackInfoReturnable<Boolean> ci) {
        if (entity instanceof LivingEntity living)
            if (YSNSAPI.isEntityDisabled(living, living.level())) {
                living.discard();
                ci.setReturnValue(false);
            }
    }
}

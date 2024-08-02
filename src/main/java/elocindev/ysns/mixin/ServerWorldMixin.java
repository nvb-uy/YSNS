package elocindev.ysns.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import elocindev.ysns.api.YSNSAPI;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

@Mixin(ServerLevel.class)
public class ServerWorldMixin {
    @Inject(method = "addEntity", at = @At("HEAD"))
    private void ysns$addEntity(Entity entity, CallbackInfoReturnable<Boolean> ci) {
        if (YSNSAPI.isEntityDisabled(entity, entity.level())) {
            entity.discard();
            ci.setReturnValue(false);
        }
    }
}

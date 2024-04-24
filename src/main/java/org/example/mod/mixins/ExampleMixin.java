package org.example.mod.mixins;

import dev.crmodders.flux.logging.LogWrapper;
import finalforeach.cosmicreach.BlockGame;
import org.example.mod.ExampleMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockGame.class)
public class ExampleMixin {

    private static String TAG = "\u001b[35;1m{"+ ExampleMod.MOD_ID+"/MIXINS}\u001b[0m\u001b[37m";

    // Mix into the top of the method "public void create()" in the class BlockGame
    @Inject(method = "create()V", at = @At("HEAD"))
    public void create(CallbackInfo ci) {
        LogWrapper.info("%s: LOOK MOM IM HERE BEFORE INIT".formatted(TAG));
    }

    // Mix into the bottom of the method "public void create()" in the class BlockGame
    @Inject(method = "create()V", at = @At("TAIL"))
    public void createTAIL(CallbackInfo ci) {
        LogWrapper.info("%s: LOOK MOM IM HERE AFTER INIT".formatted(TAG));
    }

}

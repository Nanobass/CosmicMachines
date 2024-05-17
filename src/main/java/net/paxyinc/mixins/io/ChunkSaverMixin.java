package net.paxyinc.mixins.io;

import finalforeach.cosmicreach.io.ChunkSaver;
import finalforeach.cosmicreach.world.World;
import net.paxyinc.io.FunctionalBlockSaveSystem;
import net.paxyinc.io.NbtEntitySaveSystem;
import net.paxyinc.io.PlayerSaveSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

@Mixin(ChunkSaver.class)
public class ChunkSaverMixin {

    @Shadow public static boolean isSaving;

    @Inject(method = "saveWorld", at = @At("HEAD"), cancellable = true)
    private static void cancelSaveWorldIfSaving(World world, CallbackInfo ci) {
        if(isSaving) ci.cancel();
    }

    @Redirect(method = "saveWorld", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/io/EntitySaveSystem;savePlayers(Lfinalforeach/cosmicreach/world/World;)V"))
    private static void saveWorld(World world) {
        PlayerSaveSystem.savePlayers(world);
        try {
            FunctionalBlockSaveSystem.Instance.saveWorld(world);
            NbtEntitySaveSystem.Instance.saveWorld(world);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

package net.paxyinc.machines.mixins.io;

import finalforeach.cosmicreach.io.ChunkSaver;
import finalforeach.cosmicreach.world.World;
import net.paxyinc.machines.entities.FunctionalBlock;
import net.paxyinc.machines.io.FunctionalBlockSaveSystem;
import net.paxyinc.machines.io.NbtEntitySaveSystem;
import net.paxyinc.machines.io.PlayerSaveSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.IOException;

@Mixin(ChunkSaver.class)
public class ChunkSaverMixin {

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

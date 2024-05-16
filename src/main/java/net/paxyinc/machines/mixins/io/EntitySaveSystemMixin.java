package net.paxyinc.machines.mixins.io;

import finalforeach.cosmicreach.io.EntitySaveSystem;
import finalforeach.cosmicreach.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(EntitySaveSystem.class)
public class EntitySaveSystemMixin {

    @Overwrite
    public static void savePlayers(World world) {
        System.err.println("Usage of savePlayers is deprecated");
    }

    @Overwrite
    public static void loadPlayers(World world) {
        System.err.println("Usage of loadPlayers is deprecated");
    }

}

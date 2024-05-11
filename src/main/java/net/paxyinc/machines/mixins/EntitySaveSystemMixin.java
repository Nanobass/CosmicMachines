package net.paxyinc.machines.mixins;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import finalforeach.cosmicreach.entities.Player;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.io.EntitySaveSystem;
import finalforeach.cosmicreach.io.SaveLocation;
import finalforeach.cosmicreach.world.World;
import net.paxyinc.machines.io.AdvancedEntitySaveSystem;
import net.paxyinc.machines.nbt.NbtSerializable;
import net.paxyinc.machines.nbt.NbtSerializer;
import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.io.NamedTag;
import net.querz.nbt.tag.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

@Mixin(EntitySaveSystem.class)
public class EntitySaveSystemMixin {

    @Overwrite
    public static void savePlayers(World world) {
        AdvancedEntitySaveSystem.saveEntities(world);
        AdvancedEntitySaveSystem.saveTileEntities(world);
        AdvancedEntitySaveSystem.savePlayers(world);
        System.err.println("Usage of savePlayers is deprecated");
    }

    @Overwrite
    public static void loadPlayers(World world) {
        System.err.println("Usage of loadPlayers is deprecated");
    }

}

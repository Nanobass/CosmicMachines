package net.paxyinc.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import finalforeach.cosmicreach.entities.Player;
import finalforeach.cosmicreach.world.World;
import finalforeach.cosmicreach.world.Zone;
import net.paxyinc.interfaces.WorldInterface;
import net.paxyinc.nbt.NbtSerializable;
import net.paxyinc.nbt.NbtSerializer;
import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.io.NamedTag;
import net.querz.nbt.tag.CompoundTag;

import java.util.UUID;

public class PlayerSaveSystem {

    public static String localPlayerFileName = "players/localPlayer.dat";

    private static FileHandle getPlayerFile(World world, UUID uuid) {
        return Gdx.files.absolute(world.getFullSaveFolder() + "/players/" + uuid.toString() + ".dat");
    }

    private static FileHandle getEntityFile(Zone zone, UUID uuid) {
        return Gdx.files.absolute(zone.getFullSaveFolder() + "/entities/" + uuid.toString() + ".dat");
    }

    private static FileHandle getEntitiesFolder(Zone zone) {
        return Gdx.files.absolute(zone.getFullSaveFolder() + "/entities/");
    }

    private static FileHandle getTileEntityFile(Zone zone, UUID uuid) {
        return Gdx.files.absolute(zone.getFullSaveFolder() + "/tile_entities/" + uuid.toString() + ".dat");
    }

    private static FileHandle getTileEntitiesFolder(Zone zone) {
        return Gdx.files.absolute(zone.getFullSaveFolder() + "/tile_entities/");
    }

    public static void savePlayers(World world) {
        WorldInterface wi = (WorldInterface) world;
        for(UUID uuid : wi.getPlayers().keySet()) {
            Player player = wi.getPlayer(uuid);
            savePlayer(world, uuid, player);
        }
    }

    public static void savePlayer(World world, UUID uuid, Player player) {
        FileHandle file = getPlayerFile(world, uuid);
        try {
            file.file().getParentFile().mkdirs();
            file.file().createNewFile();
            CompoundTag tag = NbtSerializer.write((NbtSerializable) player);
            NBTUtil.write(tag, file.file());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Player loadPlayer(World world, UUID uuid) {
        FileHandle file = getPlayerFile(world, uuid);
        try {
            NamedTag tag = NBTUtil.read(file.file());
            if(tag.getTag() instanceof CompoundTag compound) {
                return NbtSerializer.read(compound);
            } else {
                throw new RuntimeException("Unknown entity file");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

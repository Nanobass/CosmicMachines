package net.paxyinc.machines.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import finalforeach.cosmicreach.entities.Entity;
import finalforeach.cosmicreach.entities.Player;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.io.SaveLocation;
import finalforeach.cosmicreach.world.World;
import finalforeach.cosmicreach.world.Zone;
import net.paxyinc.machines.entities.BetterEntity;
import net.paxyinc.machines.entities.TileEntity;
import net.paxyinc.machines.interfaces.WorldInterface;
import net.paxyinc.machines.interfaces.ZoneInterface;
import net.paxyinc.machines.nbt.NbtSerializable;
import net.paxyinc.machines.nbt.NbtSerializer;
import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.io.NamedTag;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.Tag;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.io.File;
import java.util.*;

public class AdvancedEntitySaveSystem {

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

    public static void saveEntities(World world) {
        WorldInterface wi = (WorldInterface) world;
        for(Zone zone : world.getZones()) {
            ZoneInterface zi = (ZoneInterface) zone;
            List<BetterEntity> entities = zi.getEntities();
            for(BetterEntity entity : entities) {
                saveEntity(zone, entity);
            }
        }
    }

    public static void loadEntities(World world) {
        WorldInterface wi = (WorldInterface) world;
        for(Zone zone : world.getZones()) {
            ZoneInterface zi = (ZoneInterface) zone;
            FileHandle folder = getEntitiesFolder(zone);
            for(FileHandle file : folder.list()) {
                UUID uuid = UUID.fromString(file.nameWithoutExtension());
                BetterEntity entity = loadEntity(zone, uuid);
                zi.addEntity(entity);
            }
        }
    }

    public static void saveTileEntities(World world) {
        WorldInterface wi = (WorldInterface) world;
        for(Zone zone : world.getZones()) {
            ZoneInterface zi = (ZoneInterface) zone;
            List<TileEntity> entities = zi.getTileEntities();
            for(TileEntity entity : entities) {
                saveTileEntity(zone, entity);
            }
        }
    }

    public static void loadTileEntities(World world) {
        for(Zone zone : world.getZones()) {
            ZoneInterface zi = (ZoneInterface) zone;
            FileHandle folder = getTileEntitiesFolder(zone);
            for(FileHandle file : folder.list()) {
                UUID uuid = UUID.fromString(file.nameWithoutExtension());
                TileEntity entity = loadTileEntity(zone, uuid);
                entity.initialize(null);
                zi.addTileEntity(entity);
            }
        }
        for(Zone zone : world.getZones()) {
            ZoneInterface zi = (ZoneInterface) zone;
            for(TileEntity entity : zi.getTileEntities()) {
                entity.onCreate(zone);
            }
        }
    }

    public static BetterEntity loadEntity(Zone zone, UUID uuid) {
        FileHandle file = getEntityFile(zone, uuid);
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

    public static void saveEntity(Zone zone, BetterEntity entity) {
        FileHandle file = getEntityFile(zone, entity.uuid());
        try {
            file.file().getParentFile().mkdirs();
            file.file().createNewFile();
            CompoundTag tag = NbtSerializer.write(entity);
            NBTUtil.write(tag, file.file());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteEntity(Zone zone, BetterEntity entity) {
        FileHandle file = getEntityFile(zone, entity.uuid());
        try {
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static TileEntity loadTileEntity(Zone zone, UUID uuid) {
        FileHandle file = getTileEntityFile(zone, uuid);
        try {
            NamedTag tag = NBTUtil.read(file.file());
            if(tag.getTag() instanceof CompoundTag compound) {
                return NbtSerializer.read(compound);
            } else {
                throw new RuntimeException("Unknown tile_entity file");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void saveTileEntity(Zone zone, TileEntity entity) {
        FileHandle file = getTileEntityFile(zone, entity.uuid());
        try {
            file.file().getParentFile().mkdirs();
            file.file().createNewFile();
            CompoundTag tag = NbtSerializer.write(entity);
            NBTUtil.write(tag, file.file());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteTileEntity(Zone zone, TileEntity entity) {
        FileHandle file = getTileEntityFile(zone, entity.uuid());
        try {
            file.delete();
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

}

package net.paxyinc.machines.io;

import finalforeach.cosmicreach.world.Chunk;
import finalforeach.cosmicreach.world.Region;
import net.paxyinc.machines.entities.BetterEntity;
import net.paxyinc.machines.interfaces.ChunkInterface;
import net.paxyinc.machines.nbt.NbtSerializer;
import net.paxyinc.region.RegionChunkFile;
import net.paxyinc.region.RegionFile;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;
import net.querz.nbt.tag.Tag;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class NbtEntitySaveSystem extends SaveSystem<BetterEntity> {

    public static final NbtEntitySaveSystem Instance = new NbtEntitySaveSystem();

    @Override
    public RegionFile getRegionFile(Region region) throws IOException {
        String folder = "entities";
        String format = "r_%d_%d_%d.dat";
        String fileName = folder + "/" + format.formatted(region.regionX, region.regionY, region.regionZ);
        File file = new File(region.zone.getFullSaveFolder(), fileName);
        File parent = file.getParentFile();
        if(!parent.exists()) parent.mkdirs();
        return new RegionFile(file, region.regionX, region.regionY, region.regionZ);
    }

    @Override
    public boolean isEmpty(Region region) {
        for(Chunk chunk : region.getChunks()) {
            ChunkInterface ci = (ChunkInterface) chunk;
            if(!ci.getEntities().isEmpty()) return false;
            for(BetterEntity entity : ci.getEntities().values()) {
                if(entity.savable) return false;
            }
        }
        return true;
    }

    @Override
    public void saveRegionChunks(Region region, List<Chunk> chunks) throws IOException {
        RegionFile regionFile = getCachedRegionFile(region, true);
        Map<Integer, Chunk> indexed = new HashMap<>();
        for(Chunk chunk : chunks) {
            indexed.put(getChunkIndex(chunk), chunk);
        }
        regionFile.writeChunks((index, os) -> {
            Chunk chunk = indexed.get(index);
            if(chunk != null) {
                RegionChunkFile file = getRegionChunkFile(regionFile, chunk);
                Tag<?> nbt = write(chunk);
                if(nbt != null) file.write(nbt, os);
                return nbt != null;
            }
            return false;
        });
        closeFile(region);
    }

    @Override
    public void loadRegionChunks(Region region, List<Chunk> chunks) throws IOException {
        RegionFile regionFile = getCachedRegionFile(region, false);
        if(regionFile == null) return;
        for(Chunk chunk : chunks) {
            RegionChunkFile file = getRegionChunkFile(regionFile, chunk);
            if(!file.exists()) continue;
            Tag<?> nbt = file.read();
            read(chunk, nbt);
        }
        closeFile(region);
    }

    public Tag<?> write(Chunk chunk) throws IOException {
        ChunkInterface ci = (ChunkInterface) chunk;
        ListTag<CompoundTag> entityTags = new ListTag<>(CompoundTag.class);

        Map<UUID, BetterEntity> entities = ci.getEntities();
        if(entities.isEmpty()) return null;
        for(var entry : entities.entrySet()) {
            BetterEntity entity = entry.getValue();
            if(!entity.savable) continue;
            CompoundTag entityTag = NbtSerializer.write(entity);
            entityTags.add(entityTag);
        }

        return entityTags;
    }

    public void read(Chunk chunk, Tag<?> tag) throws IOException {
        ChunkInterface ci = (ChunkInterface) chunk;
        ListTag<CompoundTag> entityTags = (ListTag<CompoundTag>) tag;

        List<BetterEntity> entities = new ArrayList<>();
        for(CompoundTag entityTag : entityTags) {
            BetterEntity entity = NbtSerializer.read(entityTag);
            entities.add(entity);
        }

        entities.forEach(ci::addEntity);
    }

}

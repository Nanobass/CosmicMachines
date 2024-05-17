package net.paxyinc.io;

import finalforeach.cosmicreach.world.Chunk;
import finalforeach.cosmicreach.world.Region;
import finalforeach.cosmicreach.world.World;
import finalforeach.cosmicreach.world.Zone;
import net.paxyinc.nbt.NbtSerializable;
import net.paxyinc.region.RegionChunkFile;
import net.paxyinc.region.RegionFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

public abstract class SaveSystem<T extends NbtSerializable> {

    public void saveWorld(World world) throws IOException {
        for(Zone zone : world.getZones()) {
            saveZone(zone);
        }
    }

    public void saveZone(Zone zone) throws IOException {
        for(Region region : zone.getRegions()) {
            saveRegionChunks(region, StreamSupport.stream(region.getChunks().spliterator(), false).toList());
        }
    }

    public void saveChunk(Chunk chunk) throws IOException {
        saveRegionChunks(chunk.region, List.of(chunk));
    }

    public void loadChunk(Chunk chunk) throws IOException {
        loadRegionChunks(chunk.region, List.of(chunk));
    }

    public void saveChunks(Iterable<Chunk> chunks) throws IOException {
        Map<Region, List<Chunk>> buckets = new HashMap<>();
        for(Chunk chunk : chunks) {
            if(buckets.containsKey(chunk.region)) {
                List<Chunk> bucket = buckets.get(chunk.region);
                bucket.add(chunk);
            } else {
                List<Chunk> bucket = new ArrayList<>();
                bucket.add(chunk);
                buckets.put(chunk.region, bucket);
            }
        }
        buckets.keySet().removeIf(this::isEmpty);
        for(var entry : buckets.entrySet()) {
            saveRegionChunks(entry.getKey(), entry.getValue());
        }
    }

    public void loadChunks(Iterable<Chunk> chunks) throws IOException {
        Map<Region, List<Chunk>> buckets = new HashMap<>();
        for(Chunk chunk : chunks) {
            if(buckets.containsKey(chunk.region)) {
                List<Chunk> bucket = buckets.get(chunk.region);
                bucket.add(chunk);
            } else {
                List<Chunk> bucket = new ArrayList<>();
                bucket.add(chunk);
                buckets.put(chunk.region, bucket);
            }
        }
        for(var entry : buckets.entrySet()) {
            loadRegionChunks(entry.getKey(), entry.getValue());
        }
    }

    protected int getChunkIndex(Chunk chunk) {
        int x = chunk.chunkX - chunk.region.regionX * 16;
        int y = chunk.chunkY - chunk.region.regionY * 16;
        int z = chunk.chunkZ - chunk.region.regionZ * 16;
        return RegionFile.getChunkIndex(x, y, z);
    }

    public abstract RegionFile getRegionFile(Region region) throws IOException;

    public RegionChunkFile getRegionChunkFile(RegionFile regionFile, Chunk chunk) {
        return new RegionChunkFile(regionFile, chunk.chunkX - regionFile.getRegionX() * 16, chunk.chunkY - regionFile.getRegionY() * 16, chunk.chunkZ - regionFile.getRegionZ() * 16);
    }

    private final Map<Region, RegionFile> openFiles = new HashMap<>();
    public boolean cacheEnabled = true;

    public RegionFile getCachedRegionFile(Region region, boolean createElse) throws IOException {
        RegionFile file = openFiles.get(region);
        if(file == null && createElse) {
            file = getRegionFile(region);
            openFiles.put(region, file);
        }
        return file;
    }

    public void closeFile(Region region) throws IOException {
        if(cacheEnabled) return;
        RegionFile file =  openFiles.remove(region);
        if(file != null) file.close();
    }

    public void clearCache() throws IOException {
        if(!cacheEnabled) return;
        for(RegionFile file : openFiles.values()) {
            file.close();
        }
        openFiles.clear();
    }

    protected abstract boolean isEmpty(Region region);
    protected abstract void saveRegionChunks(Region region, List<Chunk> chunks) throws IOException;
    protected abstract void loadRegionChunks(Region region, List<Chunk> chunks) throws IOException;

}

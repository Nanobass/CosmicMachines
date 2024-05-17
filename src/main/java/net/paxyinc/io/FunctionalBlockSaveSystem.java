package net.paxyinc.io;

import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.world.Chunk;
import finalforeach.cosmicreach.world.Region;
import net.paxyinc.entities.FunctionalBlock;
import net.paxyinc.interfaces.ChunkInterface;
import net.paxyinc.nbt.NbtSerializer;
import net.paxyinc.region.RegionChunkFile;
import net.paxyinc.region.RegionFile;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;
import net.querz.nbt.tag.Tag;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FunctionalBlockSaveSystem extends SaveSystem<FunctionalBlock> {

    public static final FunctionalBlockSaveSystem Instance = new FunctionalBlockSaveSystem();

    @Override
    public RegionFile getRegionFile(Region region) throws IOException {
        String folder = "functional_blocks";
        String format = "r_%d_%d_%d.dat";
        String fileName = folder + "/" + format.formatted(region.regionX, region.regionY, region.regionZ);
        File file = new File(region.zone.getFullSaveFolder(), fileName);
        File parent = file.getParentFile();
        if(!parent.exists()) parent.mkdirs();
        return new RegionFile(file, region.regionX, region.regionY, region.regionZ);
    }

    @Override
    protected boolean isEmpty(Region region) {
        for(Chunk chunk : region.getChunks()) {
            ChunkInterface ci = (ChunkInterface) chunk;
            if(!ci.getFunctionalBlocks().isEmpty()) return false;
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
        RegionFile regionFile = getCachedRegionFile(region, true);
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

        ListTag<CompoundTag> blockTags = new ListTag<>(CompoundTag.class);
        Map<BlockPosition, FunctionalBlock> blocks = ci.getFunctionalBlocks();
        if(blocks.isEmpty()) return null;
        for(var entry : blocks.entrySet()) {
            FunctionalBlock block = entry.getValue();
            CompoundTag blockTag = NbtSerializer.write(block);
            blockTags.add(blockTag);
        }
        return blockTags;
    }

    public void read(Chunk chunk, Tag<?> tag) throws IOException {
        ChunkInterface ci = (ChunkInterface) chunk;

        ListTag<CompoundTag> blockTags = (ListTag<CompoundTag>) tag;
        List<FunctionalBlock> blocks = new ArrayList<>();
        for(CompoundTag blockTag : blockTags) {
            FunctionalBlock block = NbtSerializer.read(blockTag);
            block.initialize(blockTag, chunk);
            blocks.add(block);
        }
        blocks.forEach(ci::addFunctionalBlock);
    }

}

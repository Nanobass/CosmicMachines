package net.paxyinc.mixins.interfaces;

import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.savelib.blockdata.IBlockData;
import finalforeach.cosmicreach.savelib.lightdata.blocklight.IBlockLightData;
import finalforeach.cosmicreach.savelib.lightdata.skylight.ISkylightData;
import finalforeach.cosmicreach.world.Chunk;
import finalforeach.cosmicreach.world.IChunkMeshGroup;
import finalforeach.cosmicreach.world.Region;
import net.paxyinc.entities.BetterEntity;
import net.paxyinc.entities.FunctionalBlock;
import net.paxyinc.interfaces.ChunkInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mixin(Chunk.class)
public class ChunkMixin implements ChunkInterface {

    @Shadow public Region region;
    @Shadow public IChunkMeshGroup<?> meshGroup;
    @Shadow public int chunkX;
    @Shadow public int chunkY;
    @Shadow public int chunkZ;
    @Shadow public int blockX;
    @Shadow public int blockY;
    @Shadow public int blockZ;
    @Shadow public boolean isGenerated;
    @Shadow public IBlockData<BlockState> blockData;
    @Shadow public IBlockLightData blockLightData;
    @Shadow public ISkylightData skyLightData;
    @Shadow public transient boolean isSaved;

    private final Map<UUID, BetterEntity> entities = new HashMap<>();
    private final Map<BlockPosition, FunctionalBlock> functionalBlocks = new HashMap<>();

    @Override
    public void update(float deltaTime) {
        synchronized (functionalBlocks) {
            for(Map.Entry<BlockPosition, FunctionalBlock> entry : functionalBlocks.entrySet()) {
                FunctionalBlock block = entry.getValue();
                block.onTick();
            }
        }
        synchronized (entities) {
            for(Map.Entry<UUID, BetterEntity> entry : entities.entrySet()) {
                BetterEntity entity = entry.getValue();
                entity.update(region.zone, deltaTime);
            }
        }
    }

    @Override
    public void addEntity(BetterEntity entity) {
        synchronized (entities) {
            entities.put(entity.uuid, entity);
        }
    }

    @Override
    public void removeEntity(UUID uuid) {
        synchronized (entities) {
            entities.remove(uuid);
        }
    }

    @Override
    public BetterEntity getEntity(UUID uuid) {
        synchronized (entities) {
            return entities.get(uuid);
        }
    }

    @Override
    public Map<UUID, BetterEntity> getEntities() {
        return entities;
    }

    @Override
    public void addFunctionalBlock(FunctionalBlock block) {
        synchronized (functionalBlocks) {
            functionalBlocks.put(block.position, block);
        }
    }

    @Override
    public FunctionalBlock removeFunctionalBlock(BlockPosition position) {
        synchronized (functionalBlocks) {
            return functionalBlocks.remove(position);
        }
    }

    @Override
    public FunctionalBlock getFunctionalBlock(BlockPosition position) {
        synchronized (functionalBlocks) {
            return functionalBlocks.get(position);
        }
    }

    @Override
    public Map<BlockPosition, FunctionalBlock> getFunctionalBlocks() {
        return functionalBlocks;
    }


}

package net.paxyinc.mixins.interfaces;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import dev.crmodders.flux.api.block.IModBlock;
import dev.crmodders.flux.registry.FluxRegistries;
import dev.crmodders.flux.registry.registries.AccessableRegistry;
import dev.crmodders.flux.tags.Identifier;
import finalforeach.cosmicreach.blockevents.ScheduledTrigger;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.constants.Direction;
import finalforeach.cosmicreach.entities.Entity;
import finalforeach.cosmicreach.entities.Player;
import finalforeach.cosmicreach.world.*;
import finalforeach.cosmicreach.worldgen.ZoneGenerator;
import net.paxyinc.entities.BetterEntity;
import net.paxyinc.entities.FunctionalBlock;
import net.paxyinc.entities.IFunctionalModBlock;
import net.paxyinc.interfaces.ChunkInterface;
import net.paxyinc.interfaces.ZoneInterface;
import net.paxyinc.util.DirectionUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(Zone.class)
public abstract class ZoneMixin implements ZoneInterface {

    @Shadow
    public PriorityQueue<ScheduledTrigger> eventQueue;
    @Shadow
    public int currentTick = 0;
    @Shadow
    private @Final Map<ChunkCoords, Chunk> chunks;
    @Shadow
    public @Final Map<RegionCoords, Region> regions;
    @Shadow
    public Array<Entity> allEntities;
    @Shadow
    public Vector3 spawnPoint;
    @Shadow
    public String zoneId;
    @Shadow
    public ZoneGenerator zoneGenerator;
    @Shadow
    public float respawnHeight;
    @Shadow
    private transient World world;
    private final Map<UUID, BetterEntity> entities = new HashMap<>();

    private Chunk getBottomMostChunk(Chunk chunk) {
        return getChunkAtChunkCoords(chunk.chunkX, 0, chunk.chunkZ);
    }

    @Override
    public void update(float deltaTime) {
        runScheduledTriggers();

        List<BetterEntity> toUnload = new ArrayList<>();
        synchronized (entities) {
            for (Map.Entry<UUID, BetterEntity> entry : entities.entrySet()) {
                BetterEntity entity = entry.getValue();
                int entityLastChunkX = Math.floorDiv((int) entity.lastPosition.x, 16);
                int entityLastChunkZ = Math.floorDiv((int) entity.lastPosition.z, 16);
                int entityChunkX = Math.floorDiv((int) entity.position.x, 16);
                int entityChunkZ = Math.floorDiv((int) entity.position.z, 16);
                // entity moved between chunks
                if (entityChunkX != entityLastChunkX || entityChunkZ != entityLastChunkZ || entity.chunk == null) {
                    Chunk oldChunk = entity.chunk;
                    Chunk newChunk = getChunkAtChunkCoords(entityChunkX, 0, entityChunkZ);
                    if (oldChunk == null && newChunk != null) {
                        // entity went back into still loaded chunks, by teleporting, or by loading
                        ChunkInterface newCi = (ChunkInterface) newChunk;
                        newCi.addEntity(entity);
                        entity.chunk = newChunk;
                    }
                    if (oldChunk != null && newChunk != null) {
                        // entity went between chunks
                        ChunkInterface oldCi = (ChunkInterface) oldChunk;
                        ChunkInterface newCi = (ChunkInterface) newChunk;
                        oldCi.removeEntity(entity.uuid);
                        newCi.addEntity(entity);
                        entity.chunk = newChunk;
                    }
                    if (oldChunk != null && newChunk == null) {
                        // entity went into unloaded chunks
                        ChunkInterface oldCi = (ChunkInterface) oldChunk;
                        oldCi.removeEntity(entity.uuid);
                        entity.chunk = null;
                        toUnload.add(entity);
                    }
                }
            }

        }

        for (BetterEntity entity : toUnload) {
            // TODO do this, region file has to change, nope it doesn't have to, well, idk when ima do dat
        }

        synchronized (chunks) {
            // TODO reduce iteration count here
            for (Map.Entry<ChunkCoords, Chunk> entry : chunks.entrySet()) {
                Chunk chunk = entry.getValue();
                ChunkInterface ci = (ChunkInterface) chunk;
                ci.update(deltaTime);
            }
        }


    }


    @Inject(method = "addChunk", at = @At("TAIL"))
    private void addChunk(Chunk chunk, CallbackInfo ci_) {
        Chunk bottom = getBottomMostChunk(chunk);
        ChunkInterface ci = (ChunkInterface) chunk;
        Map<UUID, BetterEntity> loaded = ci.getEntities();
        for (Map.Entry<UUID, BetterEntity> entry : loaded.entrySet()) {
            BetterEntity entity = entry.getValue();
            entity.chunk = bottom;
        }
        synchronized (entities) {
            entities.putAll(loaded);
        }
    }

    @Inject(method = "removeChunk", at = @At("TAIL"))
    private void removeChunk(Chunk chunk, CallbackInfo ci_) {
        Chunk bottom = getBottomMostChunk(chunk);
        if(bottom != null) {
            ChunkInterface ci = (ChunkInterface) bottom;
            Map<UUID, BetterEntity> loaded = ci.getEntities();
            synchronized (entities) {
                loaded.forEach(entities::remove);
            }
            for (Map.Entry<UUID, BetterEntity> entry : loaded.entrySet()) {
                BetterEntity entity = entry.getValue();
                entity.chunk = null;
            }
        }
        ChunkInterface ci = (ChunkInterface) chunk;
        Map<BlockPosition, FunctionalBlock> blocks = ci.getFunctionalBlocks();
        for(var entry : blocks.entrySet()) {
            FunctionalBlock block = entry.getValue();
            block.position = null;
        }
        blocks.clear();
    }

    @Override
    public BetterEntity spawnEntity(Class<?> clazz, UUID uuid, float x, float y, float z) {
        try {
            BetterEntity entity = (BetterEntity) clazz.getDeclaredConstructor().newInstance();
            entity.setPosition(x, y, z);
            addEntity(entity);
            return entity;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void spawnEntity(BetterEntity entity, float x, float y, float z) {
        try {
            entity.setPosition(x, y, z);
            addEntity(entity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addEntity(BetterEntity entity) {
        synchronized (entities) {
            entities.put(entity.uuid, entity);
        }
    }

    @Override
    public void killEntity(UUID uuid) {
        synchronized (entities) {
            BetterEntity entity = entities.remove(uuid);
            if (entity.chunk != null) {
                ChunkInterface ci = (ChunkInterface) entity.chunk;
                ci.removeEntity(uuid);
                entity.chunk = null;
            }
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
    public FunctionalBlock getFunctionalBlock(BlockPosition position) {
        ChunkInterface ci = (ChunkInterface) position.chunk;
        return ci.getFunctionalBlock(position);
    }


    @Override
    public <T extends FunctionalBlock> Map<Direction, T> forEachNeighbor(BlockPosition pos, Class<T> clazz) {
        Map<Direction, T> neighbors = new HashMap<>();
        Zone zone = pos.chunk.region.zone; // SHUT UP INTELLIJ THE CODE IS REACHABLE
        for(Direction face : Direction.values()) {
            BlockPosition neighborPos = pos.getOffsetBlockPos(zone, face);
            FunctionalBlock neighborBlock = getFunctionalBlock(neighborPos);
            if(clazz.isInstance(neighborBlock)) {
                neighbors.put(face, (T) neighborBlock);
            }
        }
        return neighbors;
    }

    @Override
    public void onBlockPlaced(BlockPosition pos, BlockState block, double timeSinceLast) {
        ChunkInterface ci = (ChunkInterface) pos.chunk;
        AccessableRegistry<IModBlock> blocks = FluxRegistries.BLOCKS.access();
        Identifier blockId = Identifier.fromString(block.getBlockId());
        IModBlock modBlock = blocks.get(blockId);
        if(modBlock instanceof IFunctionalModBlock functionalModBlock) {

            FunctionalBlock functionalBlock = functionalModBlock.createFunctionalBlock();
            functionalBlock.position = pos;
            functionalBlock.blockState = block;
            functionalBlock.block = block.getBlock();
            functionalBlock.onCreate();
            ci.addFunctionalBlock(functionalBlock);

            Zone zone = pos.chunk.region.zone; // SHUT UP INTELLIJ THE CODE IS REACHABLE
            for(Direction face : Direction.values()) {
                BlockPosition neighborPos = pos.getOffsetBlockPos(zone, face);
                FunctionalBlock neighborBlock = getFunctionalBlock(neighborPos);
                if(neighborBlock != null) {
                    neighborBlock.onNeighborPlaced(DirectionUtil.opposite(face), functionalBlock);
                }
                functionalBlock.onNeighborPlaced(face, neighborBlock);
            }

        }
    }

    @Override
    public void onBlockBroken(BlockPosition pos, double timeSinceLast) {
        ChunkInterface ci = (ChunkInterface) pos.chunk;
        FunctionalBlock functionalBlock = ci.removeFunctionalBlock(pos);
        if(functionalBlock != null) {
            functionalBlock.onDestroy();

            Zone zone = pos.chunk.region.zone; // SHUT UP INTELLIJ THE CODE IS REACHABLE
            for(Direction face : Direction.values()) {
                BlockPosition neighborPos = pos.getOffsetBlockPos(zone, face);
                FunctionalBlock neighborBlock = getFunctionalBlock(neighborPos);
                if(neighborBlock != null) {
                    neighborBlock.onNeighborBroken(DirectionUtil.opposite(face), functionalBlock);
                }
                functionalBlock.onNeighborBroken(face, neighborBlock);
            }
        }
    }

    @Override
    public void onBlockInteract(BlockPosition pos, Player player, double timeSinceLast) {
        ChunkInterface ci = (ChunkInterface) pos.chunk;
        FunctionalBlock functionalBlock = ci.getFunctionalBlock(pos);
        if(functionalBlock != null) {
            functionalBlock.onInteract(player);
        }
    }

    @Shadow
    protected abstract void runScheduledTriggers();

    @Shadow
    protected abstract Chunk getChunkAtChunkCoords(int cx, int cy, int cz);

}

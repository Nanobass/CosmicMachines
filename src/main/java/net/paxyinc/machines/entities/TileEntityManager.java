package net.paxyinc.machines.entities;

import dev.crmodders.flux.api.events.GameEvents;
import dev.crmodders.flux.api.events.system.Event;
import dev.crmodders.flux.api.events.system.EventFactory;
import dev.crmodders.flux.registry.registries.AccessableRegistry;
import dev.crmodders.flux.registry.registries.DynamicRegistry;
import dev.crmodders.flux.tags.Identifier;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.constants.Direction;
import finalforeach.cosmicreach.entities.Player;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.gamestates.LoadingGame;
import finalforeach.cosmicreach.rendering.IZoneRenderer;
import finalforeach.cosmicreach.world.ChunkCoords;
import finalforeach.cosmicreach.world.RegionCoords;
import finalforeach.cosmicreach.world.WorldLoader;
import finalforeach.cosmicreach.world.Zone;
import net.paxyinc.machines.util.DirectionUtil;

import java.util.*;
import java.util.function.Supplier;

public class TileEntityManager implements GameEvents.GameEventTriggers.OnGameTickedTrigger, GameEvents.GameEventTriggers.OnBlockPlacedTrigger, GameEvents.GameEventTriggers.OnBlockBrokenTrigger {

    public static final TileEntityManager MANAGER = new TileEntityManager();

    public static final DynamicRegistry<Supplier<TileEntity>> BLOCK_TILE_ENTITY_FACTORIES = DynamicRegistry.create();
    public static final AccessableRegistry<Supplier<TileEntity>> BLOCK_TILE_ENTITY_FACTORIES_ACCESS = BLOCK_TILE_ENTITY_FACTORIES.access();

    public static final Event<OnTileEntityCreated> ON_TILE_ENTITY_CREATED_EVENT = EventFactory.createArrayBacked(OnTileEntityCreated.class, array ->
            (zone, block, entity, pos, timeSinceLast) -> {
                for(OnTileEntityCreated event : array) event.onTileEntityCreated(zone, block, entity, pos, timeSinceLast);
            }
    );

    public static final Event<OnTileEntityDestroyed> ON_TILE_ENTITY_DESTROYED_EVENT = EventFactory.createArrayBacked(OnTileEntityDestroyed.class, array ->
            (zone, entity, pos, timeSinceLast) -> {
                for(OnTileEntityDestroyed event : array) event.onTileEntityDestroyed(zone, entity, pos, timeSinceLast);
            }
    );

    public Map<BlockPosition, TileEntity> entitiesByBlock = new HashMap<>();
    public Map<ChunkCoords, List<TileEntity>> entitiesByChunk = new HashMap<>();
    public Map<RegionCoords, List<TileEntity>> entitiesByRegion = new HashMap<>();

    public <T extends TileEntity> Map<Direction, T> forEachNeighbor(Zone zone, BlockPosition pos, Class<T> clazz) {
        Map<Direction, T> neighbors = new HashMap<>();
        for(Direction face : Direction.values()) {
            BlockPosition neighborPos = pos.getOffsetBlockPos(zone, face);
            if(entitiesByBlock.containsKey(neighborPos)) {
                TileEntity neighborEntity = entitiesByBlock.get(neighborPos);
                if(clazz.isInstance(neighborEntity)) {
                    neighbors.put(face, (T) neighborEntity);
                }
            }
        }
        return neighbors;
    }

    @Override
    public void onTick() {
        if(!WorldLoader.worldLoader.readyToPlay) return;
        for(Map.Entry<BlockPosition, TileEntity> entry : entitiesByBlock.entrySet()) {
            TileEntity entity = entry.getValue();
            // TODO, do this properly
            Player player = InGame.getLocalPlayer();
            Zone playerZone = player.getZone(InGame.world);
            entity.onTick(playerZone);
        }
    }

    public void onWorldLoaded() {

    }

    public void onWorldUnloaded() {

    }

    @Override
    public void onBlockPlaced(Zone zone, BlockState block, BlockPosition pos, double timeSinceLast) {
        Identifier blockId = Identifier.fromString(block.getBlock().getStringId());
        TileEntity entity = null;
        if(BLOCK_TILE_ENTITY_FACTORIES_ACCESS.contains(blockId)) {
            entity = BLOCK_TILE_ENTITY_FACTORIES_ACCESS.get(blockId).get();
            entity.position = pos;
            entity.block = block.getBlock();
            entity.onCreate(zone);
            entitiesByBlock.put(pos, entity);
            ON_TILE_ENTITY_CREATED_EVENT.invoker().onTileEntityCreated(zone, block, entity, pos, timeSinceLast);
        }
        for(Direction face : Direction.values()) {
            BlockPosition neighborPos = pos.getOffsetBlockPos(zone, face);
            TileEntity neighborEntity = null;
            if(entitiesByBlock.containsKey(neighborPos)) {
                neighborEntity = entitiesByBlock.get(neighborPos);
                neighborEntity.onNeighborPlaced(zone, DirectionUtil.opposite(face), entity);
            }
            if(entity != null) {
                entity.onNeighborPlaced(zone, face, neighborEntity);
            }
        }
    }

    @Override
    public void onBlockBroken(Zone zone, BlockPosition pos, double timeSinceLast) {
        TileEntity entity = entitiesByBlock.remove(pos);
        if(entity != null) ON_TILE_ENTITY_DESTROYED_EVENT.invoker().onTileEntityDestroyed(zone, entity, pos, timeSinceLast);
        for(Direction face : Direction.values()) {
            BlockPosition neighborPos = pos.getOffsetBlockPos(zone, face);
            if(entitiesByBlock.containsKey(neighborPos)) {
                TileEntity neighborEntity = entitiesByBlock.get(neighborPos);
                neighborEntity.onNeighborBroken(zone, DirectionUtil.opposite(face), entity);
            }
        }
        if(entity != null ) {
            entity.onDestroy(zone);
        }
    }

    public void onBlockInteract(Zone zone, BlockPosition position, Player player, double timeSinceLast) {
        TileEntity entity = entitiesByBlock.get(position);
        if(entity != null) entity.onInteract(zone, player);
    }

}

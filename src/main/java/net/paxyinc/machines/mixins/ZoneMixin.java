package net.paxyinc.machines.mixins;

import dev.crmodders.flux.tags.Identifier;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.constants.Direction;
import finalforeach.cosmicreach.entities.Entity;
import finalforeach.cosmicreach.entities.Player;
import finalforeach.cosmicreach.world.WorldLoader;
import finalforeach.cosmicreach.world.Zone;
import net.paxyinc.machines.entities.BetterEntity;
import net.paxyinc.machines.entities.TileEntity;
import net.paxyinc.machines.interfaces.ZoneInterface;
import net.paxyinc.machines.io.AdvancedEntitySaveSystem;
import net.paxyinc.machines.util.DirectionUtil;
import org.spongepowered.asm.mixin.Mixin;

import java.util.*;

import static net.paxyinc.machines.entities.TileEntityRegistry.BLOCK_TILE_ENTITY_FACTORIES_ACCESS;

@Mixin(Zone.class)
public class ZoneMixin implements ZoneInterface {

    private Map<BlockPosition, TileEntity> allTileEntities = new HashMap<>();

    @Override
    public void update(float deltaTime) {
        Zone z = (Zone) (Object) this;
        z.runScheduledTriggers();
        for (Entity e : z.allEntities) {
            e.update(z, deltaTime);
        }

        if(WorldLoader.worldLoader.readyToPlay) {
            for(Map.Entry<BlockPosition, TileEntity> entry : allTileEntities.entrySet()) {
                TileEntity entity = entry.getValue();
                entity.onTick(z);
            }
        }
    }

    @Override
    public <T extends TileEntity> Map<Direction, T> forEachNeighbor(Zone zone, BlockPosition pos, Class<T> clazz) {
        Map<Direction, T> neighbors = new HashMap<>();
        for(Direction face : Direction.values()) {
            BlockPosition neighborPos = pos.getOffsetBlockPos(zone, face);
            if(allTileEntities.containsKey(neighborPos)) {
                TileEntity neighborEntity = allTileEntities.get(neighborPos);
                if(clazz.isInstance(neighborEntity)) {
                    neighbors.put(face, (T) neighborEntity);
                }
            }
        }
        return neighbors;
    }

    @Override
    public void onBlockPlaced(Zone zone, BlockState block, BlockPosition pos, double timeSinceLast) {
        Identifier blockId = Identifier.fromString(block.getBlock().getStringId());
        TileEntity entity = null;
        if(BLOCK_TILE_ENTITY_FACTORIES_ACCESS.contains(blockId)) {
            entity = BLOCK_TILE_ENTITY_FACTORIES_ACCESS.get(blockId).get();
            entity.initialize(pos);
            entity.onCreate(zone);
            allTileEntities.put(pos, entity);
        }
        for(Direction face : Direction.values()) {
            BlockPosition neighborPos = pos.getOffsetBlockPos(zone, face);
            TileEntity neighborEntity = null;
            if(allTileEntities.containsKey(neighborPos)) {
                neighborEntity = allTileEntities.get(neighborPos);
                neighborEntity.onNeighborPlaced(zone, DirectionUtil.opposite(face), entity);
            }
            if(entity != null) {
                entity.onNeighborPlaced(zone, face, neighborEntity);
            }
        }
    }

    @Override
    public void onBlockBroken(Zone zone, BlockPosition pos, double timeSinceLast) {
        TileEntity entity = allTileEntities.get(pos);
        for(Direction face : Direction.values()) {
            BlockPosition neighborPos = pos.getOffsetBlockPos(zone, face);
            if(allTileEntities.containsKey(neighborPos)) {
                TileEntity neighborEntity = allTileEntities.get(neighborPos);
                neighborEntity.onNeighborBroken(zone, DirectionUtil.opposite(face), entity);
            }
        }

        if(entity != null) {
            entity.onDestroy(zone);
            deleteTileEntity(entity.uuid());
        }
    }

    public void onBlockInteract(Zone zone, BlockPosition position, Player player, double timeSinceLast) {
        TileEntity entity = allTileEntities.get(position);
        if(entity != null) entity.onInteract(zone, player);
    }

    @Override
    public TileEntity loadTileEntity(Class<?> clazz, UUID uuid) {
        Zone zone = (Zone) (Object) this;
        try {
            TileEntity entity = AdvancedEntitySaveSystem.loadTileEntity(zone, uuid);
            if(entity == null) {
                entity = (TileEntity) clazz.getDeclaredConstructor().newInstance();
            }
            entity.initialize(null);
            addTileEntity(entity);
            return entity;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addTileEntity(TileEntity entity) {
        Zone zone = (Zone) (Object) this;
        allTileEntities.put(entity.getPosition(zone), entity);
    }

    @Override
    public void unloadTileEntity(UUID uuid) {
        Zone zone = (Zone) (Object) this;
        TileEntity entity = getTileEntity(uuid);
        if(entity != null) allTileEntities.remove(entity.getPosition(zone));
        else return;
        AdvancedEntitySaveSystem.saveTileEntity(zone, entity);
    }

    @Override
    public void deleteTileEntity(UUID uuid) {
        Zone zone = (Zone) (Object) this;
        TileEntity entity = getTileEntity(uuid);
        if(entity != null) allTileEntities.remove(entity.getPosition(zone));
        else return;
        AdvancedEntitySaveSystem.deleteTileEntity(zone, entity);
    }

    @Override
    public TileEntity getTileEntity(UUID uuid) {
        Zone zone = (Zone) (Object) this;
        for(TileEntity entity : allTileEntities.values()) {
            if(entity.uuid == uuid) return entity;
        }
        return null;
    }

    @Override
    public List<TileEntity> getTileEntities() {
        Zone zone = (Zone) (Object) this;
        return new ArrayList<>(allTileEntities.values());
    }


    @Override
    public BetterEntity loadEntity(Class<?> clazz, UUID uuid) {
        Zone zone = (Zone) (Object) this;
        try {
            BetterEntity entity = AdvancedEntitySaveSystem.loadEntity(zone, uuid);
            if(entity == null) {
                entity = (BetterEntity) clazz.getDeclaredConstructor().newInstance();
                entity.uuid = uuid;
            }
            addEntity(entity);
            return entity;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addEntity(BetterEntity entity) {
        Zone zone = (Zone) (Object) this;
        zone.allEntities.add(entity);
    }

    @Override
    public void unloadEntity(UUID uuid) {
        Zone zone = (Zone) (Object) this;
        BetterEntity entity = getEntity(uuid);
        if(entity != null) zone.allEntities.removeValue(entity, true);
        else return;
        AdvancedEntitySaveSystem.saveEntity(zone, entity);
    }

    @Override
    public void deleteEntity(UUID uuid) {
        Zone zone = (Zone) (Object) this;
        BetterEntity entity = getEntity(uuid);
        if(entity != null) zone.allEntities.removeValue(entity, true);
        else return;
        AdvancedEntitySaveSystem.deleteEntity(zone, entity);
    }

    @Override
    public BetterEntity getEntity(UUID uuid) {
        Zone zone = (Zone) (Object) this;
        for(Entity entity : zone.allEntities) {
            if(entity instanceof BetterEntity betterEntity && betterEntity.uuid == uuid) return betterEntity;
        }
        return null;
    }

    @Override
    public List<BetterEntity> getEntities() {
        Zone zone = (Zone) (Object) this;
        List<BetterEntity> entities = new ArrayList<>();
        for(Entity entity : zone.allEntities) {
            if(entity instanceof BetterEntity betterEntity) entities.add(betterEntity);
        }
        return entities;
    }
}

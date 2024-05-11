package net.paxyinc.machines.interfaces;

import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.constants.Direction;
import finalforeach.cosmicreach.entities.Player;
import finalforeach.cosmicreach.world.Zone;
import net.paxyinc.machines.entities.BetterEntity;
import net.paxyinc.machines.entities.TileEntity;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ZoneInterface {

    BetterEntity loadEntity(Class<?> clazz, UUID uuid);
    void addEntity(BetterEntity entity);
    void unloadEntity(UUID uuid);
    void deleteEntity(UUID uuid);
    BetterEntity getEntity(UUID uuid);
    List<BetterEntity> getEntities();
    TileEntity loadTileEntity(Class<?> clazz, UUID uuid);
    void addTileEntity(TileEntity entity);
    void unloadTileEntity(UUID uuid);
    void deleteTileEntity(UUID uuid);
    TileEntity getTileEntity(UUID uuid);
    List<TileEntity> getTileEntities();

    void update(float deltaTime);
    <T extends TileEntity> Map<Direction, T> forEachNeighbor(Zone zone, BlockPosition pos, Class<T> clazz);
    void onBlockPlaced(Zone zone, BlockState block, BlockPosition pos, double timeSinceLast);
    void onBlockBroken(Zone zone, BlockPosition pos, double timeSinceLast);
    void onBlockInteract(Zone zone, BlockPosition position, Player player, double timeSinceLast);
    
}

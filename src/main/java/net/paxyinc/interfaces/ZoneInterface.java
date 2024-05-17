package net.paxyinc.interfaces;

import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.constants.Direction;
import finalforeach.cosmicreach.entities.Player;
import net.paxyinc.entities.BetterEntity;
import net.paxyinc.entities.FunctionalBlock;

import java.util.Map;
import java.util.UUID;

public interface ZoneInterface {

    void update(float deltaTime);

    BetterEntity spawnEntity(Class<?> clazz, UUID uuid, float x, float y, float z);
    void spawnEntity(BetterEntity entity, float x, float y, float z);
    void addEntity(BetterEntity entity);
    void killEntity(UUID uuid);
    BetterEntity getEntity(UUID uuid);
    Map<UUID, BetterEntity> getEntities();

    FunctionalBlock getFunctionalBlock(BlockPosition position);

    void onBlockPlaced(BlockPosition pos, BlockState block, double timeSinceLast);
    void onBlockBroken(BlockPosition pos, double timeSinceLast);
    void onBlockInteract(BlockPosition position, Player player, double timeSinceLast);

    <T extends FunctionalBlock> Map<Direction, T> forEachNeighbor(BlockPosition pos, Class<T> clazz);

}

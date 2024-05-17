package net.paxyinc.interfaces;

import finalforeach.cosmicreach.blocks.BlockPosition;
import net.paxyinc.entities.BetterEntity;
import net.paxyinc.entities.FunctionalBlock;

import java.util.Map;
import java.util.UUID;

public interface ChunkInterface {
    void update(float deltaTime);

    void addEntity(BetterEntity entity);
    void removeEntity(UUID uuid);
    BetterEntity getEntity(UUID uuid);
    Map<UUID, BetterEntity> getEntities();

    void addFunctionalBlock(FunctionalBlock block);
    FunctionalBlock removeFunctionalBlock(BlockPosition position);
    FunctionalBlock getFunctionalBlock(BlockPosition position);
    Map<BlockPosition, FunctionalBlock> getFunctionalBlocks();

}

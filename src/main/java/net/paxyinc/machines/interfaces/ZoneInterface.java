package net.paxyinc.machines.interfaces;

import finalforeach.cosmicreach.world.Zone;
import net.paxyinc.machines.entities.BetterEntity;

import java.util.List;
import java.util.UUID;

public interface ZoneInterface {

    BetterEntity loadEntity(Class<?> clazz, UUID uuid);
    void addEntity(BetterEntity entity);
    void unloadEntity(UUID uuid);
    void deleteEntity(UUID uuid);
    BetterEntity getEntity(UUID uuid);
    List<BetterEntity> getEntities();
    
}

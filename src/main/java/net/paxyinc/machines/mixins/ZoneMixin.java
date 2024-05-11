package net.paxyinc.machines.mixins;

import finalforeach.cosmicreach.entities.Entity;
import finalforeach.cosmicreach.world.World;
import finalforeach.cosmicreach.world.Zone;
import net.paxyinc.machines.entities.BetterEntity;
import net.paxyinc.machines.interfaces.ZoneInterface;
import net.paxyinc.machines.io.AdvancedEntitySaveSystem;
import org.spongepowered.asm.mixin.Mixin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mixin(Zone.class)
public class ZoneMixin implements ZoneInterface {
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

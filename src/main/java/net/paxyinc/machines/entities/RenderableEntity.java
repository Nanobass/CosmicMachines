package net.paxyinc.machines.entities;

import com.badlogic.gdx.graphics.Camera;
import finalforeach.cosmicreach.entities.Entity;
import finalforeach.cosmicreach.entities.Player;
import finalforeach.cosmicreach.world.Zone;

public class RenderableEntity extends Entity {

    public static void spawn(Zone zone, float x, float y, float z, Entity entity) {
        entity.position.set(x, y, z);
        zone.allEntities.add(entity);
    }

    public void render(Camera camera) {}

    public void despawn(Zone zone) {
        zone.allEntities.removeValue(this, true);
    }

}

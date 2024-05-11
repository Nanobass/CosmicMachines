package net.paxyinc.machines.interfaces;

import finalforeach.cosmicreach.entities.Entity;
import finalforeach.cosmicreach.world.World;
import net.paxyinc.machines.entities.BetterEntity;

import java.util.UUID;

public interface PlayerInterface {
    UUID getControlledEntityUUID();
    void initialize(World world, BetterEntity controlledEntity);
}

package net.paxyinc.entities;

import finalforeach.cosmicreach.entities.Entity;
import finalforeach.cosmicreach.world.Chunk;
import finalforeach.cosmicreach.world.ChunkCoords;
import finalforeach.cosmicreach.world.RegionCoords;
import net.paxyinc.nbt.NbtSerializable;
import net.paxyinc.util.NbtUtil;
import net.querz.nbt.tag.CompoundTag;

import java.util.UUID;

public class BetterEntity extends Entity implements NbtSerializable {

    public UUID uuid = UUID.randomUUID();
    public transient Chunk chunk;
    public transient boolean savable = true;

    public int health = 100;
    public int oxygen = 100;

    public void hurt(int damage) {
        health -= damage;
    }

    public void heal(int heal) {
        health += heal;
    }

    public boolean dead() {
        return health <= 0;
    }

    public UUID uuid() {
        return uuid;
    }

    public RegionCoords getRegionPosition() {
        int regionX = Math.floorDiv((int) position.x, 256);
        int regionY = Math.floorDiv((int) position.y, 256);
        int regionZ = Math.floorDiv((int) position.z, 256);
        return new RegionCoords(regionX, regionY, regionZ);
    }

    public ChunkCoords getChunkPositionYZero() {
        int chunkX = Math.floorDiv((int) position.x, 16);
        int chunkZ = Math.floorDiv((int) position.z, 16);
        return new ChunkCoords(chunkX, 0, chunkZ);
    }

    public ChunkCoords getChunkPosition() {
        int chunkX = Math.floorDiv((int) position.x, 16);
        int chunkY = Math.floorDiv((int) position.y, 16);
        int chunkZ = Math.floorDiv((int) position.z, 16);
        return new ChunkCoords(chunkX, chunkY, chunkZ);
    }

    public Chunk getChunk() {
        return chunk;
    }

    @Override
    public void write(CompoundTag nbt) {
        nbt.putString("uuid", uuid.toString());
        nbt.putFloat("gravityModifier", gravityModifier);
        nbt.putBoolean("hasGravity", hasGravity);
        nbt.putBoolean("isOnGround", isOnGround);
        nbt.putBoolean("collidedX", collidedX);
        nbt.putBoolean("collidedZ", collidedZ);
        nbt.putBoolean("noClip", noClip);
        nbt.putFloat("maxStepHeight", maxStepHeight);
        nbt.putFloat("fluidImmersionRatio", fluidImmersionRatio);
        nbt.putBoolean("isSneaking", isSneaking);
        nbt.put("viewDirection", NbtUtil.serializeVector3(viewDirection));
        nbt.put("position", NbtUtil.serializeVector3(position));
        nbt.put("lastPosition", NbtUtil.serializeVector3(lastPosition));
        nbt.put("viewPositionOffset", NbtUtil.serializeVector3(viewPositionOffset));
        // nbt.put("acceleration", NbtUtil.serializeVector3(acceleration));
        nbt.put("velocity", NbtUtil.serializeVector3(velocity));
        nbt.put("onceVelocity", NbtUtil.serializeVector3(onceVelocity));
        nbt.put("localBoundingBox", NbtUtil.serializeBoundingBox(localBoundingBox));
        nbt.putBoolean("savable", savable);
        nbt.putInt("health", health);
        nbt.putInt("oxygen", oxygen);
    }

    @Override
    public void read(CompoundTag nbt) {
        uuid = UUID.fromString(nbt.getString("uuid"));
        gravityModifier = nbt.getFloat("gravityModifier");
        hasGravity = nbt.getBoolean("hasGravity");
        isOnGround = nbt.getBoolean("isOnGround");
        collidedX = nbt.getBoolean("collidedX");
        collidedZ = nbt.getBoolean("collidedZ");
        noClip = nbt.getBoolean("noClip");
        maxStepHeight = nbt.getFloat("maxStepHeight");
        fluidImmersionRatio = nbt.getFloat("fluidImmersionRatio");
        isSneaking = nbt.getBoolean("isSneaking");
        viewDirection = NbtUtil.deserializeVector3(nbt.get("viewDirection"));
        position = NbtUtil.deserializeVector3(nbt.get("position"));
        lastPosition = NbtUtil.deserializeVector3(nbt.get("lastPosition"));
        viewPositionOffset = NbtUtil.deserializeVector3(nbt.get("viewPositionOffset"));
        // acceleration = NbtUtil.deserializeVector3(nbt.get("acceleration"));
        velocity = NbtUtil.deserializeVector3(nbt.get("velocity"));
        onceVelocity = NbtUtil.deserializeVector3(nbt.get("onceVelocity"));
        localBoundingBox = NbtUtil.deserializeBoundingBox(nbt.get("localBoundingBox"));
        savable = nbt.getBoolean("savable");
        health = nbt.getInt("health");
        oxygen = nbt.getInt("oxygen");
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

}

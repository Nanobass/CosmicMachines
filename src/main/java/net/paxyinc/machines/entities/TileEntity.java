package net.paxyinc.machines.entities;

import com.badlogic.gdx.math.Vector3;
import dev.crmodders.flux.api.block.IModBlock;
import dev.crmodders.flux.util.BlockPositionUtil;
import finalforeach.cosmicreach.blocks.Block;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.constants.Direction;
import finalforeach.cosmicreach.entities.Player;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.world.Chunk;
import finalforeach.cosmicreach.world.Zone;
import net.paxyinc.machines.MachineMod;
import net.paxyinc.machines.interfaces.ZoneInterface;
import net.paxyinc.machines.nbt.NbtSerializable;
import net.paxyinc.machines.util.NbtUtil;
import net.querz.nbt.tag.CompoundTag;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TileEntity implements NbtSerializable, ITileEntityCapabilities {

    public int x, y, z;
    protected Map<Direction, TileEntity> neighbors = new HashMap<>();
    public UUID uuid = UUID.randomUUID();

    public UUID uuid() {
        return uuid;
    }

    public void onCreate(Zone zone) {
        ZoneInterface zi = (ZoneInterface) zone;
        neighbors = zi.forEachNeighbor(zone, getPosition(zone), TileEntity.class);
    }

    public void onNeighborPlaced(Zone zone, Direction face, TileEntity entity) {
        neighbors.put(face, entity);
    }

    public void onNeighborBroken(Zone zone, Direction face, TileEntity entity) {
        neighbors.remove(face, entity);
    }

    public void onInteract(Zone zone, Player player) {

    }

    public void onTick(Zone zone) {

    }

    public void onDestroy(Zone zone) {

    }

    @Override
    public boolean canAcceptItems(Direction face) {
        return false;
    }

    @Override
    public boolean canOutputItems(Direction face) {
        return false;
    }

    @Override
    public boolean canAcceptEnergy(Direction face) {
        return false;
    }

    @Override
    public boolean canOutputEnergy(Direction face) {
        return false;
    }

    @Override
    public boolean canAcceptFluids(Direction face) {
        return false;
    }

    @Override
    public boolean canOutputFluids(Direction face) {
        return false;
    }

    @Override
    public void write(CompoundTag nbt) {
        nbt.putString("uuid", uuid.toString());
        Vector3 tmpPosition = new Vector3(x, y, z);
        nbt.put("position", NbtUtil.serializeVector3(tmpPosition));
    }

    @Override
    public void read(CompoundTag nbt) {
        uuid = UUID.fromString(nbt.getString("uuid"));
        Vector3 tmpPosition = NbtUtil.deserializeVector3(nbt.get("position"));
        x = (int) tmpPosition.x;
        y = (int) tmpPosition.y;
        z = (int) tmpPosition.z;
    }

    public void initialize(BlockPosition pos) {
        if(pos != null) {
            x = pos.getGlobalX();
            y = pos.getGlobalY();
            z = pos.getGlobalZ();
        }
    }

    public static BlockPosition getBlockPositionAtGlobalPos(Zone zone, int x, int y, int z) {
        Chunk c = zone.getChunkAtBlock(x, y, z);
        if(c == null) return null;
        return new BlockPosition(c, x - c.blockX, y - c.blockY, z - c.blockZ);
    }

    public BlockPosition getPosition(Zone zone) {
        return getBlockPositionAtGlobalPos(zone, x, y, z);
    }

}

package net.paxyinc.machines.entities;

import dev.crmodders.flux.api.block.IModBlock;
import finalforeach.cosmicreach.blocks.Block;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.constants.Direction;
import finalforeach.cosmicreach.entities.Player;
import finalforeach.cosmicreach.world.Zone;
import net.paxyinc.machines.MachineMod;
import net.paxyinc.machines.nbt.NbtSerializable;
import net.querz.nbt.tag.CompoundTag;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TileEntity implements NbtSerializable<TileEntity>, ITileEntityCapabilities {

    protected BlockPosition position;
    protected BlockState blockState;
    protected Block block;
    protected Map<Direction, TileEntity> neighbors = new HashMap<>();

    public void onCreate(Zone zone) {

    }

    public void onNeighborPlaced(Zone zone, Direction face, TileEntity entity) {
        neighbors.put(face, entity);
    }

    public void onNeighborBroken(Zone zone, Direction face, TileEntity entity) {
        neighbors.remove(face);
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
    public void read(CompoundTag nbt) {
    }

    @Override
    public void write(CompoundTag nbt) {
    }

}

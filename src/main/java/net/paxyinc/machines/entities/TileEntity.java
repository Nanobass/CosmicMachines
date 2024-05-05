package net.paxyinc.machines.entities;

import finalforeach.cosmicreach.blocks.Block;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.constants.Direction;
import finalforeach.cosmicreach.world.Zone;
import net.paxyinc.machines.MachineMod;
import net.paxyinc.machines.nbt.NbtSerializable;
import net.querz.nbt.tag.CompoundTag;

public class TileEntity implements NbtSerializable<TileEntity> {

    protected BlockPosition position;
    protected Block block;

    public void onNeighborPlaced(Zone zone, Direction face, TileEntity entity) {

    }

    public void onNeighborBroken(Zone zone, Direction face, TileEntity entity) {

    }

    public void onCreate(Zone zone) {

    }

    public void onDestroy(Zone zone) {

    }

    public void onTick(Zone zone) {

    }

    @Override
    public void read(CompoundTag nbt) {

    }

    @Override
    public void write(CompoundTag nbt) {

    }
}

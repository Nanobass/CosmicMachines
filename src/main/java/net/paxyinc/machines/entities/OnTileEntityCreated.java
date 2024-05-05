package net.paxyinc.machines.entities;

import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.world.Zone;

public interface OnTileEntityCreated {

    void onTileEntityCreated(Zone zone, BlockState block, TileEntity entity, BlockPosition pos, double timeSinceLast);

}

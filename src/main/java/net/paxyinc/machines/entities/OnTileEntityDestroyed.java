package net.paxyinc.machines.entities;

import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.world.Zone;

public interface OnTileEntityDestroyed {

    void onTileEntityDestroyed(Zone zone, TileEntity entity, BlockPosition pos, double timeSinceLast);

}

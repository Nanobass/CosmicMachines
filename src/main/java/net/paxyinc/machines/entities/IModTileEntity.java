package net.paxyinc.machines.entities;

import dev.crmodders.flux.api.block.IModBlock;

import java.util.function.Supplier;

public interface IModTileEntity extends IModBlock {

    Supplier<TileEntity> getTileEntityFactory();

}

package net.paxyinc.machines.energy;

import finalforeach.cosmicreach.constants.Direction;
import net.paxyinc.machines.entities.TileEntity;
import net.querz.nbt.tag.CompoundTag;

public class TileEntityEnergyHandler extends TileEntity implements IEnergyConsumer, IEnergyProducer {

    protected EnergyStorage storage;

    public TileEntityEnergyHandler(int capacity, int maxConsume, int maxProduce) {
        storage = new EnergyStorage(capacity, maxConsume, maxProduce);
    }

    @Override
    public void read(CompoundTag nbt) {
        super.read(nbt);
        storage.read(nbt);
    }

    @Override
    public void write(CompoundTag nbt) {
        super.write(nbt);
        storage.write(nbt);
    }

    @Override
    public int consume(Direction from, int maximum, boolean simulate) {
        return storage.consume(maximum, simulate);
    }

    @Override
    public int produce(Direction from, int maximum, boolean simulate) {
        return storage.produce(maximum, simulate);
    }

}

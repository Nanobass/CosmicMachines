package net.paxyinc.machines.entities;

import net.paxyinc.machines.nbt.NbtSerializable;
import net.querz.nbt.tag.CompoundTag;

public class EnergyStorage implements IEnergyStorage, NbtSerializable<EnergyStorage> {

    public int stored;
    public int capacity;
    public int maxConsume;
    public int maxProduce;

    public EnergyStorage(int capacity, int maxConsume, int maxProduce) {
        this.capacity = capacity;
        this.maxConsume = maxConsume;
        this.maxProduce = maxProduce;
    }

    @Override
    public void read(CompoundTag nbt) {
        this.stored = nbt.getInt("energy");
        if(stored > capacity) {
            stored = capacity;
        }
    }

    @Override
    public void write(CompoundTag nbt) {
        if(stored < 0) {
            stored = 0;
        }
        nbt.putInt("energy", stored);
    }

    @Override
    public int consume(int amount, boolean simulate) {
        int received = Math.min(capacity - stored, Math.min(maxConsume, amount));
        if(simulate) stored += received;
        return received;
    }

    @Override
    public int produce(int amount, boolean simulate) {
        int produced = Math.min(stored, Math.min(maxProduce, amount));
        if(simulate) stored -= produced;
        return produced;
    }

    @Override
    public int available() {
        return stored;
    }

    @Override
    public int maximum() {
        return capacity;
    }

}

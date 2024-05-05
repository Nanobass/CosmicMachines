package net.paxyinc.machines.energy;

import net.paxyinc.machines.nbt.NbtSerializable;
import net.querz.nbt.tag.CompoundTag;

public class EnergyStorage implements IEnergyStorage, NbtSerializable<EnergyStorage> {

    public int energy;
    public int capacity;
    public int maxConsume;
    public int maxProduce;

    public EnergyStorage(int capacity) {
        this(capacity, capacity, capacity);
    }

    public EnergyStorage(int capacity, int maxTransfer) {
        this(capacity, maxTransfer, maxTransfer);
    }

    public EnergyStorage(int capacity, int maxConsume, int maxProduce) {
        this.capacity = capacity;
        this.maxConsume = maxConsume;
        this.maxProduce = maxProduce;
    }

    @Override
    public void read(CompoundTag nbt) {
        this.energy = nbt.getInt("energy");
        if(energy > capacity) {
            energy = capacity;
        }
    }

    @Override
    public void write(CompoundTag nbt) {
        if(energy < 0) {
            energy = 0;
        }
        nbt.putInt("energy", energy);
    }

    @Override
    public int consume(int maximum, boolean simulate) {
        int received = Math.min(capacity - energy, Math.min(maxConsume, maximum));
        if(simulate) energy += received;
        return received;
    }

    @Override
    public int produce(int maximum, boolean simulate) {
        int produced = Math.min(energy, Math.min(maxProduce, maximum));
        if(simulate) energy -= produced;
        return produced;
    }

    @Override
    public int stored() {
        return energy;
    }

    @Override
    public int maximum() {
        return capacity;
    }

}

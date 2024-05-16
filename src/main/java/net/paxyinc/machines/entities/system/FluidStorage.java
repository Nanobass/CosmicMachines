package net.paxyinc.machines.entities.system;

import net.paxyinc.machines.fluid.Fluid;
import net.paxyinc.machines.nbt.NbtSerializable;
import net.querz.nbt.tag.CompoundTag;

public class FluidStorage implements IFluidStorage, NbtSerializable {

    public Fluid fluid;
    public int stored;
    public int capacity;
    public int maxConsume;
    public int maxProduce;

    public FluidStorage(int capacity, int maxConsume, int maxProduce) {
        this.capacity = capacity;
        this.maxConsume = maxConsume;
        this.maxProduce = maxProduce;
    }

    @Override
    public void read(CompoundTag nbt) {
        stored = nbt.getInt("fluid");
        // TODO read fluid type
        if(stored > capacity) {
            stored = capacity;
        }
    }

    @Override
    public void write(CompoundTag nbt) {
        if(stored < 0) {
            stored = 0;
        }
        nbt.putInt("fluid", stored);
        // TODO write fluid type
    }

    @Override
    public int consume(Fluid fluid, int amount, boolean simulate) {
        return 0;
    }

    @Override
    public int produce(Fluid fluid, int amount, boolean simulate) {
        return 0;
    }

    @Override
    public int available(Fluid fluid) {
        return 0;
    }

    @Override
    public int maximum(Fluid fluid) {
        return 0;
    }

}

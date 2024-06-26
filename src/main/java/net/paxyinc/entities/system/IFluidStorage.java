package net.paxyinc.entities.system;

import net.paxyinc.fluid.Fluid;

public interface IFluidStorage {

    int consume(Fluid fluid, int amount, boolean simulate);
    int produce(Fluid fluid, int amount, boolean simulate);
    int available(Fluid fluid);
    int maximum(Fluid fluid);
    default int free(Fluid fluid) { return maximum(fluid) - available(fluid); }

}

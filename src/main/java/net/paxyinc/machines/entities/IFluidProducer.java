package net.paxyinc.machines.entities;

import finalforeach.cosmicreach.constants.Direction;
import net.paxyinc.machines.fluid.Fluid;

public interface IFluidProducer {

    int produce(Direction from, Fluid fluid, int amount, boolean simulate);

}

package net.paxyinc.machines.entities.system;

import finalforeach.cosmicreach.constants.Direction;
import net.paxyinc.machines.fluid.Fluid;

public interface IFluidConsumer {

    int consume(Direction from, Fluid fluid, int amount, boolean simulate);

}

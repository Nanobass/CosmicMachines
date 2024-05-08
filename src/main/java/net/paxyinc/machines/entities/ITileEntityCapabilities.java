package net.paxyinc.machines.entities;

import finalforeach.cosmicreach.constants.Direction;

public interface ITileEntityCapabilities {

    boolean canAcceptItems(Direction face);
    boolean canOutputItems(Direction face);
    boolean canAcceptEnergy(Direction face);
    boolean canOutputEnergy(Direction face);
    boolean canAcceptFluids(Direction face);
    boolean canOutputFluids(Direction face);

}

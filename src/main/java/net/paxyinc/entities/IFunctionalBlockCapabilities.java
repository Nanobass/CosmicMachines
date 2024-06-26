package net.paxyinc.entities;

import finalforeach.cosmicreach.constants.Direction;

public interface IFunctionalBlockCapabilities {

    boolean canAcceptItems(Direction face);
    boolean canOutputItems(Direction face);
    boolean canAcceptEnergy(Direction face);
    boolean canOutputEnergy(Direction face);
    boolean canAcceptFluids(Direction face);
    boolean canOutputFluids(Direction face);

}
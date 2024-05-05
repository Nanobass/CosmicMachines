package net.paxyinc.machines.energy;

import finalforeach.cosmicreach.constants.Direction;

public interface IEnergyConsumer {

    int consume(Direction from, int maximum, boolean simulate);

}

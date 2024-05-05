package net.paxyinc.machines.energy;

import finalforeach.cosmicreach.constants.Direction;

public interface IEnergyProducer {

    int produce(Direction from, int maximum, boolean simulate);

}

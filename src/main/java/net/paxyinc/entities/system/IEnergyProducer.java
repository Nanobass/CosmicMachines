package net.paxyinc.entities.system;

import finalforeach.cosmicreach.constants.Direction;

public interface IEnergyProducer {

    int produce(Direction from, int amount, boolean simulate);

}

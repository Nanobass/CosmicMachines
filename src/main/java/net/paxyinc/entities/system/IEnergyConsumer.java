package net.paxyinc.entities.system;

import finalforeach.cosmicreach.constants.Direction;

public interface IEnergyConsumer {

    int consume(Direction from, int amount, boolean simulate);

}

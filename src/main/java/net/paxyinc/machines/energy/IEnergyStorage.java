package net.paxyinc.machines.energy;

public interface IEnergyStorage {

    int consume(int maximum, boolean simulate);

    int produce(int maximum, boolean simulate);

    int stored();

    int maximum();

    default int available() { return maximum() - stored(); }

}

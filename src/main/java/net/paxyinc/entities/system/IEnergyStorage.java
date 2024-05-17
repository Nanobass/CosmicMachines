package net.paxyinc.entities.system;

public interface IEnergyStorage {

    int consume(int amount, boolean simulate);

    int produce(int amount, boolean simulate);

    int available();

    int maximum();

    default int free() { return maximum() - available(); }

}

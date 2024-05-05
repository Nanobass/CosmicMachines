package net.paxyinc.machines.energy.base;

import net.paxyinc.machines.energy.TileEntityEnergyHandler;

public class BaseBattery extends TileEntityEnergyHandler {
    public BaseBattery(int capacity, int maxConsume, int maxProduce) {
        super(capacity, maxConsume, maxProduce);
    }
}

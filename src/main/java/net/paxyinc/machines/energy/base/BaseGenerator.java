package net.paxyinc.machines.energy.base;

import finalforeach.cosmicreach.constants.Direction;
import finalforeach.cosmicreach.world.Zone;
import net.paxyinc.machines.energy.TileEntityEnergyHandler;

public class BaseGenerator extends TileEntityEnergyHandler {

    protected int energyProducedWhenTicked;

    public BaseGenerator(int capacity, int maxConsume, int maxProduce, int energyProducedWhenTicked) {
        super(capacity, maxConsume, maxProduce);
        this.energyProducedWhenTicked = energyProducedWhenTicked;
    }

    public boolean canGenerate() {
        return true;
    }

    @Override
    public void onTick(Zone zone) {
        if(canGenerate()) storage.consume(energyProducedWhenTicked, true);
    }

    @Override
    public int consume(Direction from, int maximum, boolean simulate) {
        return 0;
    }
}

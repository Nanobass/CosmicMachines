package net.paxyinc.machines.energy.base;

import finalforeach.cosmicreach.constants.Direction;
import finalforeach.cosmicreach.world.Zone;
import net.paxyinc.machines.energy.TileEntityEnergyHandler;

public class BaseMachine extends TileEntityEnergyHandler {

    protected int energyNeededToTick;

    public BaseMachine(int capacity, int maxConsume, int maxProduce, int energyNeededToTick) {
        super(capacity, maxConsume, maxProduce);
        this.energyNeededToTick = energyNeededToTick;
    }


    @Override
    public void onTick(Zone zone) {
        int canProduce = storage.produce(energyNeededToTick, false);
        if(canProduce >= energyNeededToTick) {
            storage.produce(canProduce, true);
            onPoweredTick(zone);
        }
    }

    public void onPoweredTick(Zone zone) {
        System.out.println("I'M POWERED @" + energyNeededToTick + "SF/T " + storage.stored() + "/" + storage.maximum() + " SF");
    }

    @Override
    public int produce(Direction from, int maximum, boolean simulate) {
        return 0;
    }
}
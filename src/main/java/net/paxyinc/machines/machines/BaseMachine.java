package net.paxyinc.machines.machines;

import finalforeach.cosmicreach.constants.Direction;
import finalforeach.cosmicreach.world.Zone;
import net.paxyinc.machines.entities.*;

import java.util.HashMap;
import java.util.Map;

public class BaseMachine extends TileEntity implements IEnergyConsumer {

    protected EnergyStorage battery;
    protected Map<Direction, SideType> energySides = SideType.createSideTypes(SideType.Input);
    protected int energyNeededToTick;

    public BaseMachine(int capacity, int maxConsume, int maxProduce, int energyNeededToTick) {
        battery = new EnergyStorage(capacity, maxConsume, maxProduce);
        this.energyNeededToTick = energyNeededToTick;
    }


    @Override
    public void onTick(Zone zone) {
        int canProduce = battery.produce(energyNeededToTick, false);
        if(canProduce >= energyNeededToTick) {
            battery.produce(canProduce, true);
            onPoweredTick(zone);
        }

    }

    public void onPoweredTick(Zone zone) {
        System.out.println("I'M POWERED @" + energyNeededToTick + "SF/T " + battery.available() + "/" + battery.maximum() + " SF");
    }

    @Override
    public int consume(Direction from, int amount, boolean simulate) {
        return canAcceptEnergy(from) ? battery.consume(amount, simulate) : 0;
    }

    @Override
    public boolean canAcceptEnergy(Direction face) {
        return energySides.get(face).input;
    }
}
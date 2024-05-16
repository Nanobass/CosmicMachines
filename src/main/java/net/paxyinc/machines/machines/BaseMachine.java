package net.paxyinc.machines.machines;

import finalforeach.cosmicreach.constants.Direction;
import net.paxyinc.machines.entities.FunctionalBlock;
import net.paxyinc.machines.entities.system.EnergyStorage;
import net.paxyinc.machines.entities.system.IEnergyConsumer;
import net.paxyinc.machines.entities.system.SideType;
import net.querz.nbt.tag.CompoundTag;

import java.util.Map;

public class BaseMachine extends FunctionalBlock implements IEnergyConsumer {

    protected EnergyStorage battery;
    protected Map<Direction, SideType> energySides = SideType.createSideTypes(SideType.Input);
    protected int energyNeededToTick;

    public BaseMachine(int capacity, int maxConsume, int maxProduce, int energyNeededToTick) {
        battery = new EnergyStorage(capacity, maxConsume, maxProduce);
        this.energyNeededToTick = energyNeededToTick;
    }

    @Override
    public void read(CompoundTag nbt) {
        super.read(nbt);
        battery.read(nbt);
        // TODO energySides
    }

    @Override
    public void write(CompoundTag nbt) {
        super.write(nbt);
        battery.write(nbt);
        // TODO energySides
    }

    @Override
    public void onTick() {
        super.onTick();
        int canProduce = battery.produce(energyNeededToTick, false);
        if(canProduce >= energyNeededToTick) {
            battery.produce(canProduce, true);
            onPoweredTick();
        }

    }

    public void onPoweredTick() {
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
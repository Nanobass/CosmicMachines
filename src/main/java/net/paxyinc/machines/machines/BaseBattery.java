package net.paxyinc.machines.machines;

import finalforeach.cosmicreach.constants.Direction;
import net.paxyinc.machines.entities.FunctionalBlock;
import net.paxyinc.machines.entities.system.EnergyStorage;
import net.paxyinc.machines.entities.system.IEnergyConsumer;
import net.paxyinc.machines.entities.system.IEnergyProducer;
import net.paxyinc.machines.entities.system.SideType;
import net.querz.nbt.tag.CompoundTag;

import java.util.Map;

public class BaseBattery extends FunctionalBlock implements IEnergyConsumer, IEnergyProducer {

    protected EnergyStorage battery;
    protected Map<Direction, SideType> energySides = SideType.createSideTypes(SideType.Combined);

    public BaseBattery(int capacity, int maxConsume, int maxProduce) {
        battery = new EnergyStorage(capacity, maxConsume, maxProduce);
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
        int canProduce, canConsume;
        for(Map.Entry<Direction, FunctionalBlock> entry : neighbors.entrySet()) {
            Direction face = entry.getKey();
            FunctionalBlock neighbor = entry.getValue();
            if(neighbor instanceof IEnergyConsumer consumer) {
                canConsume = consumer.consume(face, battery.available(), false);
                canProduce = produce(face, Math.min(canConsume, battery.maxProduce), false);
                consumer.consume(face, canProduce, true);
                produce(face, canProduce, true);
            }
        }
    }

    @Override
    public int consume(Direction from, int amount, boolean simulate) {
        return canAcceptEnergy(from) ? battery.consume(amount, simulate) : 0;
    }

    @Override
    public int produce(Direction from, int amount, boolean simulate) {
        return canOutputEnergy(from) ? battery.produce(amount, simulate) : 0;
    }

    @Override
    public boolean canAcceptEnergy(Direction face) {
        return energySides.get(face).input;
    }

    @Override
    public boolean canOutputEnergy(Direction face) {
        return energySides.get(face).output;
    }
}

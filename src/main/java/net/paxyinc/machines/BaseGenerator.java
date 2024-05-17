package net.paxyinc.machines;

import finalforeach.cosmicreach.constants.Direction;
import net.paxyinc.entities.FunctionalBlock;
import net.paxyinc.entities.system.EnergyStorage;
import net.paxyinc.entities.system.IEnergyConsumer;
import net.paxyinc.entities.system.IEnergyProducer;
import net.paxyinc.entities.system.SideType;
import net.querz.nbt.tag.CompoundTag;

import java.util.Map;

public class BaseGenerator extends FunctionalBlock implements IEnergyProducer {

    protected EnergyStorage battery;
    protected Map<Direction, SideType> energySides = SideType.createSideTypes(SideType.Output);
    protected int energyProducedWhenTicked;

    public BaseGenerator(int capacity, int maxConsume, int maxProduce, int energyProducedWhenTicked) {
        battery = new EnergyStorage(capacity, maxConsume, maxProduce);
        this.energyProducedWhenTicked = energyProducedWhenTicked;
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

    public boolean canGenerate() {
        return true;
    }

    @Override
    public void onTick() {
        super.onTick();
        int canProduce, canConsume;

        if(canGenerate()) {
            battery.consume(energyProducedWhenTicked, true);
        }

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
    public int produce(Direction from, int amount, boolean simulate) {
        return canOutputEnergy(from) ? battery.produce(amount, simulate) : 0;
    }

    @Override
    public boolean canOutputEnergy(Direction face) {
        return energySides.get(face).output;
    }
}

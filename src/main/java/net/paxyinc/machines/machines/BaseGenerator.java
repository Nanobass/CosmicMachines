package net.paxyinc.machines.machines;

import finalforeach.cosmicreach.constants.Direction;
import finalforeach.cosmicreach.world.Zone;
import net.paxyinc.machines.entities.*;
import net.querz.nbt.tag.CompoundTag;

import java.util.HashMap;
import java.util.Map;

public class BaseGenerator extends TileEntity implements IEnergyProducer {

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
    public void onTick(Zone zone) {
        super.onTick(zone);
        int canProduce, canConsume;

        if(canGenerate()) {
            battery.consume(energyProducedWhenTicked, true);
        }

        for(Map.Entry<Direction, TileEntity> entry : neighbors.entrySet()) {
            Direction face = entry.getKey();
            TileEntity neighbor = entry.getValue();
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

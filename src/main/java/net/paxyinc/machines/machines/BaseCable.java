package net.paxyinc.machines.machines;


import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.constants.Direction;
import net.paxyinc.machines.entities.FunctionalBlock;
import net.paxyinc.machines.entities.system.EnergyStorage;
import net.paxyinc.machines.entities.system.IEnergyConsumer;
import net.paxyinc.machines.entities.system.IEnergyProducer;
import net.paxyinc.machines.util.DirectionUtil;
import net.querz.nbt.tag.CompoundTag;

import java.util.Map;

import static net.paxyinc.machines.util.DirectionUtil.ALL_DIRECTION_NAMES;
import static net.paxyinc.machines.util.DirectionUtil.ALL_DIRECTION_NAMES_INVERSE;

public class BaseCable extends FunctionalBlock implements IEnergyProducer, IEnergyConsumer {

    protected EnergyStorage battery;

    public BaseCable(int capacity, int maxTransfer) {
        battery = new EnergyStorage(capacity, maxTransfer, maxTransfer);
    }

    @Override
    public void read(CompoundTag nbt) {
        super.read(nbt);
        battery.read(nbt);
    }

    @Override
    public void write(CompoundTag nbt) {
        super.write(nbt);
        battery.write(nbt);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setModelConnection(neighbors.keySet());
        for(Map.Entry<Direction, FunctionalBlock> entry : neighbors.entrySet()) {
            Direction face = entry.getKey();
            FunctionalBlock neighbor = entry.getValue();
            if(neighbor instanceof BaseCable cable) {
                cable.addModelConnection(DirectionUtil.opposite(face));
            }
        }
    }


    @Override
    public void onNeighborPlaced(Direction face, FunctionalBlock entity) {
        super.onNeighborPlaced(face, entity);
        if(entity instanceof IEnergyProducer || entity instanceof IEnergyConsumer) {
            addModelConnection(face);
        }
    }

    @Override
    public void onNeighborBroken(Direction face, FunctionalBlock entity) {
        super.onNeighborBroken(face, entity);
        if(entity instanceof IEnergyProducer || entity instanceof IEnergyConsumer) {
            removeModelConnection(face);
        }
    }

    @Override
    public void onDestroy() {
        for(Map.Entry<Direction, FunctionalBlock> entry : neighbors.entrySet()) {
            Direction face = entry.getKey();
            FunctionalBlock neighbor = entry.getValue();
            if(neighbor instanceof BaseCable cable) {
                cable.removeModelConnection(DirectionUtil.opposite(face));
            }
        }
    }

    @Override
    public void onTick() {
        for(Map.Entry<Direction, FunctionalBlock> entry : neighbors.entrySet()) {
            Direction face = entry.getKey();
            FunctionalBlock neighbor = entry.getValue();
            if(neighbor instanceof IEnergyConsumer consumer) {
                int canProduce, canConsume;
                canConsume = consumer.consume(face, battery.available(), false);
                canProduce = produce(face, Math.min(canConsume, battery.maxProduce), false);
                consumer.consume(face, canProduce, true);
                produce(face, canProduce, true);
            }
        }
    }

    @Override
    public int consume(Direction from, int amount, boolean simulate) {
        return battery.consume(amount, simulate);
    }

    @Override
    public int produce(Direction from, int amount, boolean simulate) {
        return battery.produce(amount, simulate);
    }

    @Override
    public boolean canAcceptEnergy(Direction face) {
        return true;
    }

    @Override
    public boolean canOutputEnergy(Direction face) {
        return true;
    }


    public void setModelConnection(Iterable<Direction> faces) {
        int mask = DirectionUtil.mask(faces);
        String name = ALL_DIRECTION_NAMES.get(mask);
        position.setBlockState(block.blockStates.get(name));
    }

    public void addModelConnection(Direction toAdd) {
        BlockState state = position.getBlockState();
        int mask = ALL_DIRECTION_NAMES_INVERSE.getOrDefault(state.stringId, 63);
        mask |= DirectionUtil.mask(toAdd);
        String neighborName = ALL_DIRECTION_NAMES.get(mask);
        position.setBlockState(block.blockStates.get(neighborName));
    }

    public void removeModelConnection(Direction toRemove) {
        BlockState state = position.getBlockState();
        int mask = ALL_DIRECTION_NAMES_INVERSE.getOrDefault(state.stringId, 63);
        mask &= ~DirectionUtil.mask(toRemove);
        String neighborName = ALL_DIRECTION_NAMES.get(mask);
        position.setBlockState(block.blockStates.get(neighborName));
    }


}
package net.paxyinc.machines.machines;


import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.constants.Direction;
import finalforeach.cosmicreach.world.Zone;
import net.paxyinc.machines.entities.EnergyStorage;
import net.paxyinc.machines.entities.IEnergyConsumer;
import net.paxyinc.machines.entities.IEnergyProducer;
import net.paxyinc.machines.entities.TileEntity;
import net.paxyinc.machines.util.DirectionUtil;
import net.querz.nbt.tag.CompoundTag;

import java.util.Map;

import static net.paxyinc.machines.entities.TileEntityManager.MANAGER;
import static net.paxyinc.machines.util.DirectionUtil.ALL_DIRECTION_NAMES;
import static net.paxyinc.machines.util.DirectionUtil.ALL_DIRECTION_NAMES_INVERSE;

public class BaseCable extends TileEntity implements IEnergyProducer, IEnergyConsumer {

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
    public void onCreate(Zone zone) {
        setModelConnection(zone, neighbors.keySet());
        for(Map.Entry<Direction, TileEntity> entry : neighbors.entrySet()) {
            Direction face = entry.getKey();
            TileEntity neighbor = entry.getValue();
            if(neighbor instanceof BaseCable cable) {
                cable.addModelConnection(zone, DirectionUtil.opposite(face));
            }
        }
    }


    @Override
    public void onNeighborPlaced(Zone zone, Direction face, TileEntity entity) {
        super.onNeighborPlaced(zone, face, entity);
        if(entity instanceof IEnergyProducer || entity instanceof IEnergyConsumer) {
            addModelConnection(zone, face);
        }
    }

    @Override
    public void onNeighborBroken(Zone zone, Direction face, TileEntity entity) {
        super.onNeighborBroken(zone, face, entity);
        if(entity instanceof IEnergyProducer || entity instanceof IEnergyConsumer) {
            removeModelConnection(zone, face);
        }
    }

    @Override
    public void onDestroy(Zone zone) {
        for(Map.Entry<Direction, TileEntity> entry : neighbors.entrySet()) {
            Direction face = entry.getKey();
            TileEntity neighbor = entry.getValue();
            if(neighbor instanceof BaseCable cable) {
                cable.removeModelConnection(zone, DirectionUtil.opposite(face));
            }
        }
    }

    @Override
    public void onTick(Zone zone) {
        for(Map.Entry<Direction, TileEntity> entry : neighbors.entrySet()) {
            Direction face = entry.getKey();
            TileEntity neighbor = entry.getValue();
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


    public void setModelConnection(Zone zone, Iterable<Direction> faces) {
        int mask = DirectionUtil.mask(faces);
        String name = ALL_DIRECTION_NAMES.get(mask);
        position.setBlockState(block.blockStates.get(name));
    }

    public void addModelConnection(Zone zone, Direction toAdd) {
        BlockState state = position.getBlockState();
        int mask = ALL_DIRECTION_NAMES_INVERSE.getOrDefault(state.stringId, 63);
        mask |= DirectionUtil.mask(toAdd);
        String neighborName = ALL_DIRECTION_NAMES.get(mask);
        position.setBlockState(block.blockStates.get(neighborName));
    }

    public void removeModelConnection(Zone zone, Direction toRemove) {
        BlockState state = position.getBlockState();
        int mask = ALL_DIRECTION_NAMES_INVERSE.getOrDefault(state.stringId, 63);
        mask &= ~DirectionUtil.mask(toRemove);
        String neighborName = ALL_DIRECTION_NAMES.get(mask);
        position.setBlockState(block.blockStates.get(neighborName));
    }


}
package net.paxyinc.machines.energy.base;

import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.constants.Direction;
import finalforeach.cosmicreach.world.Zone;
import net.paxyinc.machines.energy.TileEntityEnergyHandler;
import net.paxyinc.machines.entities.TileEntity;
import net.paxyinc.machines.entities.TileEntityManager;
import net.paxyinc.machines.util.DirectionUtil;

import java.util.*;

import static net.paxyinc.machines.util.DirectionUtil.*;

public class BaseCable extends TileEntity {

    public CableNetwork network;
    public Map<Direction, TileEntityEnergyHandler> connections = new HashMap<>();

    public int capacity, maxTransfer;

    public BaseCable(int capacity, int maxTransfer) {
        this.capacity = capacity;
        this.maxTransfer = maxTransfer;
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

    @Override
    public void onCreate(Zone zone) {
        Map<Direction, BaseCable> neighbors = TileEntityManager.MANAGER.forEachNeighbor(zone, position, BaseCable.class);

        setModelConnection(zone, neighbors.keySet());

        Set<CableNetwork> networksToMerge = new HashSet<>();
        for(Map.Entry<Direction, BaseCable> entry : neighbors.entrySet()) {
            Direction face = entry.getKey();
            BaseCable cable = entry.getValue();
            cable.addModelConnection(zone, DirectionUtil.opposite(face));
            networksToMerge.add(cable.network);
        }

        if(networksToMerge.isEmpty()) {

            network = new CableNetwork();
            System.err.println("CREATE " + network);

        } else if(networksToMerge.size() == 1) {

            for(CableNetwork oldNetwork : networksToMerge) {
                network = oldNetwork;
                break;
            }
            System.err.println("APPEND " + network);

        } else {

            network = new CableNetwork();
            for(CableNetwork oldNetwork : networksToMerge) {
                network.addAll(oldNetwork);
                oldNetwork.forEachCable(networkCable -> networkCable.network = network);
                oldNetwork.clear();
            }
            System.err.println("MERGE " + network);

        }

        network.addCable(this);
    }

    @Override
    public void onNeighborPlaced(Zone zone, Direction face, TileEntity entity) {
        if(entity instanceof TileEntityEnergyHandler handler) {
            addModelConnection(zone, face);
            System.out.println("CONNECTED " + entity);
            connections.put(face, handler);
        }
    }

    @Override
    public void onNeighborBroken(Zone zone, Direction face, TileEntity entity) {
        if(entity instanceof TileEntityEnergyHandler handler) {
            removeModelConnection(zone, face);
            System.out.println("DISCONNECTED " + entity);
            connections.remove(face);
        }
    }

    @Override
    public void onDestroy(Zone zone) {
        Map<Direction, BaseCable> neighbors = TileEntityManager.MANAGER.forEachNeighbor(zone, position, BaseCable.class);

        network.removeCable(this);

        for(Map.Entry<Direction, BaseCable> entry : neighbors.entrySet()) {
            Direction face = entry.getKey();
            BaseCable cable = entry.getValue();
            cable.removeModelConnection(zone, DirectionUtil.opposite(face));
        }

        if(neighbors.isEmpty()) {
            network.clear();
        } else if(neighbors.size() > 1) {



            System.err.println("SPLIT");
            throw new RuntimeException("Not Implemented");
        }

    }

    @Override
    public void onTick(Zone zone) {
        for(Map.Entry<Direction, TileEntityEnergyHandler> entry : connections.entrySet()) {
            Direction face = entry.getKey();
            TileEntityEnergyHandler handler = entry.getValue();

            int canProduce, canConsume;
            canProduce = handler.produce(face, network.buffer.available(), false);
            canConsume = network.buffer.consume(Math.min(canProduce, maxTransfer), false);
            handler.produce(face, canConsume, true);
            network.buffer.consume(canConsume, true);

            canConsume = handler.consume(face, network.buffer.stored(), false);
            canProduce = network.buffer.produce(Math.min(canConsume, maxTransfer), false);
            handler.consume(face, canProduce, true);
            network.buffer.produce(canProduce, true);
        }

    }

}

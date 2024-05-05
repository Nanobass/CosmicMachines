package net.paxyinc.machines.energy.base;

import net.paxyinc.machines.energy.EnergyStorage;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class CableNetwork {

        public Set<BaseCable> cables = new HashSet<>();
        public EnergyStorage buffer = new EnergyStorage(0, Integer.MAX_VALUE);

        public void addCable(BaseCable cable) {
            this.cables.add(cable);
            this.buffer.capacity += cable.capacity;
        }

        public void removeCable(BaseCable cable) {
            this.cables.remove(cable);
            this.buffer.capacity -= cable.capacity;
        }

        public void forEachCable(Consumer<BaseCable> forEach) {
            this.cables.forEach(forEach);
        }

        public void addAll(CableNetwork network) {
            this.cables.addAll(network.cables);
            this.buffer.energy += network.buffer.energy;
            this.buffer.capacity += network.buffer.capacity;
        }

        public void clear() {
            System.err.println("DESTROYED " + this);
            this.cables.clear();
        }

    }
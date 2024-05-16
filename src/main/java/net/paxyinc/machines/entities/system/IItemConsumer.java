package net.paxyinc.machines.entities.system;

import finalforeach.cosmicreach.constants.Direction;
import net.paxyinc.machines.item.Item;

public interface IItemConsumer {

    int consume(Direction from, Item item, int amount, boolean simulate);

}

package net.paxyinc.machines.entities;

import finalforeach.cosmicreach.constants.Direction;
import net.paxyinc.machines.item.Item;

public interface IItemConsumer {

    int consume(Direction from, Item item, int amount, boolean simulate);

}

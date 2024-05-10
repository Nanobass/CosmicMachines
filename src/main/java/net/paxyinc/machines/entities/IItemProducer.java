package net.paxyinc.machines.entities;

import finalforeach.cosmicreach.constants.Direction;
import net.paxyinc.machines.item.Item;

public interface IItemProducer {

    int produce(Direction from, Item item, int amount, boolean simulate);

}
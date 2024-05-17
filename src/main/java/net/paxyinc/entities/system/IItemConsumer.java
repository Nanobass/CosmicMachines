package net.paxyinc.entities.system;

import finalforeach.cosmicreach.constants.Direction;
import net.paxyinc.item.Item;

public interface IItemConsumer {

    int consume(Direction from, Item item, int amount, boolean simulate);

}

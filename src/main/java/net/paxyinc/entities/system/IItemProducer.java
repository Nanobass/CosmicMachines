package net.paxyinc.entities.system;

import finalforeach.cosmicreach.constants.Direction;
import net.paxyinc.item.Item;

public interface IItemProducer {

    int produce(Direction from, Item item, int amount, boolean simulate);

}

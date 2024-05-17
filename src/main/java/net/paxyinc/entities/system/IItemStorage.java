package net.paxyinc.entities.system;

import net.paxyinc.item.Item;

public interface IItemStorage {

    int consume(Item item, int amount, boolean simulate);
    int produce(Item item, int amount, boolean simulate);
    int available(Item item);
    int maximum(Item item);
    default int free(Item item) { return maximum(item) - available(item); }

}

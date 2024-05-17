package net.paxyinc.item;

import dev.crmodders.flux.tags.Identifier;

public abstract class AbstractItemList implements Iterable<Item> {

    public boolean hasEnough(Item item, int amount) { return amount(item) >= amount; }
    public boolean hasEnough(Identifier tag, int amount) { return amount(tag) >= amount; }
    public abstract boolean hasEnoughSpace(Item item, int amount);
    public abstract boolean hasEnoughSpace(AbstractItemList list);
    public abstract AbstractItemList remove(Item item, int amount);
    public abstract AbstractItemList remove(Identifier tag, int amount);
    public abstract boolean put(Item item, int amount);
    public abstract boolean put(AbstractItemList list);
    public abstract boolean take(AbstractItemList list);
    public abstract int amount(Item item);
    public abstract int amount(Identifier tag);
    public abstract void clear();

}

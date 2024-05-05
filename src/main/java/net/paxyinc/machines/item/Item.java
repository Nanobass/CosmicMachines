package net.paxyinc.machines.item;

import dev.crmodders.flux.tags.Identifier;

import java.util.List;

public class Item {
    public Identifier itemId;
    public String name;
    public IItemView view;

    @Override
    public String toString() {
        return name;
    }
}

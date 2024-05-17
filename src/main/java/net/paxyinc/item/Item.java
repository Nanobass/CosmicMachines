package net.paxyinc.item;

import dev.crmodders.flux.tags.Identifier;

import java.util.ArrayList;
import java.util.List;

public class Item {

    public static final String ORE_DICT_ID = "oredict";
    public static final Identifier TAG_FUEL = new Identifier(ORE_DICT_ID, "fuel");


    public Identifier itemId;
    public String name;
    public IItemView view;
    public int maxStack = 64;
    public List<Identifier> tags = new ArrayList<>();

    @Override
    public String toString() {
        return name;
    }
}

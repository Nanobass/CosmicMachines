package net.paxyinc.machines.item.crafting;

import net.paxyinc.machines.item.Item;

public class Ingredient {

    public final Item item;
    public final int amount;

    public Ingredient(Item item, int amount) {
        this.item = item;
        this.amount = amount;
    }
}

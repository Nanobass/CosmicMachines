package net.paxyinc.machines.item.crafting;

import net.paxyinc.machines.item.Item;

public interface Ingredient {

    IngredientType type();

    int amount();

    boolean hasEnough();

}

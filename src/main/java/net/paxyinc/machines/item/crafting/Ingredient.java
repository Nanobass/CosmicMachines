package net.paxyinc.machines.item.crafting;

public interface Ingredient {

    IngredientType type();

    int amount();

    boolean hasEnough();

}

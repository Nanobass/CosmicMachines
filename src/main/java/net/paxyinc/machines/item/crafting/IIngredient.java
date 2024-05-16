package net.paxyinc.machines.item.crafting;

public interface IIngredient {

    IngredientType type();

    Unit unit();

    String name();

    int amount();

}

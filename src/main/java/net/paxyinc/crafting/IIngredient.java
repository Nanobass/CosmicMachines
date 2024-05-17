package net.paxyinc.crafting;

public interface IIngredient {

    IngredientType type();

    Unit unit();

    String name();

    int amount();

}

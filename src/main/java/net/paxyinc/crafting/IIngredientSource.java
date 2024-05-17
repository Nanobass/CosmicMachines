package net.paxyinc.crafting;

public interface IIngredientSource {

    boolean hasEnoughOf(IIngredient ingredient);

    void take(IIngredient ingredient);

}

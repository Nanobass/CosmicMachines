package net.paxyinc.machines.item.crafting;

public interface IIngredientSource {

    boolean hasEnoughOf(IIngredient ingredient);

    void take(IIngredient ingredient);

}

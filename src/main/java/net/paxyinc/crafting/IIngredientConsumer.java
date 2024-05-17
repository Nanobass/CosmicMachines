package net.paxyinc.crafting;

import java.util.List;

public interface IIngredientConsumer {

    boolean canTake(List<IIngredient> ingredients);

    void take(List<IIngredient> ingredients);

}

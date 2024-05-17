package net.paxyinc.crafting;

import dev.crmodders.flux.tags.Identifier;

import java.util.ArrayList;
import java.util.List;

public class Recipe {

    public final Identifier recipeId;
    public final List<IIngredient> ingredients = new ArrayList<>();
    public final List<IIngredient> results = new ArrayList<>();

    public Recipe(Identifier recipeId) {
        this.recipeId = recipeId;
    }

    public boolean canCraft(IIngredientSource source, IIngredientConsumer destination) {
        boolean canCraft = true;
        for (IIngredient ingredient : ingredients) {
            canCraft &= source.hasEnoughOf(ingredient);
        }
        return canCraft && destination.canTake(results);
    }

    public void craft(IIngredientSource source, IIngredientConsumer destination) {
        for (IIngredient ingredient : ingredients) {
            source.take(ingredient);
        }
        destination.take(results);
    }

}

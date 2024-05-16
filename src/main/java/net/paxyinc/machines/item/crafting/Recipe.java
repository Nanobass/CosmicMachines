package net.paxyinc.machines.item.crafting;

import dev.crmodders.flux.tags.Identifier;
import net.paxyinc.machines.item.ItemInventory;
import net.paxyinc.machines.item.ItemSlot;

import java.util.ArrayList;
import java.util.List;

public class Recipe {

    public final Identifier recipeId;
    public final List<Ingredient> ingredients = new ArrayList<>();
    public final List<Ingredient> result = new ArrayList<>();

    public Recipe(Identifier recipeId) {
        this.recipeId = recipeId;
    }

    public boolean canCraft(List<ItemSlot> input, ItemInventory output) {
        return false;
    }

}

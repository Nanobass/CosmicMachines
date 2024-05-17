package net.paxyinc.crafting;

import net.paxyinc.item.Item;

public class ItemIngredient implements IIngredient {

    private final Item item;
    private final int amount;

    public ItemIngredient(Item item, int amount) {
        this.item = item;
        this.amount = amount;
    }

    public Item item() {
        return item;
    }

    @Override
    public IngredientType type() {
        return IngredientType.ITEM;
    }

    @Override
    public Unit unit() {
        return Unit.PIECES;
    }

    @Override
    public String name() {
        return item.name;
    }

    @Override
    public int amount() {
        return amount;
    }
}

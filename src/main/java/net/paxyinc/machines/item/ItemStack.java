package net.paxyinc.machines.item;

public class ItemStack {
    public final Item item;
    public int amount;
    public final int max;

    public ItemStack(Item item, int amount, int max) {
        this.item = item;
        this.amount = amount;
        this.max = max;
    }

}
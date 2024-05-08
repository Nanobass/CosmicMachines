package net.paxyinc.machines.item;

import org.jetbrains.annotations.NotNull;

public class ItemSlot implements Comparable<ItemSlot> {

    public final ItemInventory container;
    public final int slotId;
    public ItemStack itemStack;

    public ItemSlot(ItemInventory container, int slotId) {
        this.container = container;
        this.slotId = slotId;
        this.itemStack = null;
    }

    @Override
    public int compareTo(ItemSlot o) {
        if(itemStack == null || o.itemStack == null) return -1;
        return Integer.compare(itemStack.amount, o.itemStack.amount);
    }
}

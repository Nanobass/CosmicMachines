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

    public boolean hasItem(Item item) {
        return itemStack != null && itemStack.item == item;
    }

    public Item getItem() {
        return itemStack != null ? itemStack.item : null;
    }

    public boolean take(int amount) {
        if(itemStack != null) {
            if(amount <= itemStack.amount) {
                itemStack.amount -= amount;
                if(itemStack.amount == 0) itemStack = null;
                return true;
            }
        }
        return false;
    }

    @Override
    public int compareTo(ItemSlot o) {
        if(itemStack == null || o.itemStack == null) return -1;
        return Integer.compare(itemStack.amount, o.itemStack.amount);
    }
}

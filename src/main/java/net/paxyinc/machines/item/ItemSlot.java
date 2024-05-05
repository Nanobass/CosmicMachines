package net.paxyinc.machines.item;

public class ItemSlot {

    public final ItemInventory container;
    public final int slotId;
    public ItemStack itemStack;

    public ItemSlot(ItemInventory container, int slotId) {
        this.container = container;
        this.slotId = slotId;
        this.itemStack = null;
    }

}

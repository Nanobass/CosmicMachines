package net.paxyinc.machines.item;

import java.util.ArrayList;
import java.util.List;

public class ItemInventory {

    public final List<ItemSlot> slots;

    public IItemInventoryRenderer renderer;

    public ItemInventory(int numSlots) {
        slots = new ArrayList<>();
        for(int i = 0; i < numSlots; i++) {
            slots.add(new ItemSlot(this, i));
        }
    }

}

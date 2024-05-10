package net.paxyinc.machines.item.inventories;

import net.paxyinc.machines.item.ItemInventory;
import net.paxyinc.machines.item.ItemSlot;
import net.paxyinc.machines.item.renderers.MouseInventoryRenderer;

public class MouseInventory extends ItemInventory {

    public static MouseInventory mouseInventory = new MouseInventory();

    public final ItemSlot slot;

    public MouseInventory() {
        super(1);
        renderer = new MouseInventoryRenderer(this);
        slot = slots.get(0);
    }
}

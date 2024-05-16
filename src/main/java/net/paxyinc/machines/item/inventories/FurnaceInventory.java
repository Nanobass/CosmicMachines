package net.paxyinc.machines.item.inventories;

import net.paxyinc.machines.item.ItemInventory;
import net.paxyinc.machines.item.renderers.FurnaceInventoryRenderer;

public class FurnaceInventory extends ItemInventory {
    public FurnaceInventory() {
        super(3);
        renderer = new FurnaceInventoryRenderer(this);
    }
}

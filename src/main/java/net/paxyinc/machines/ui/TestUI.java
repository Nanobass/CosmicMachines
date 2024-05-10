package net.paxyinc.machines.ui;

import net.paxyinc.machines.item.inventories.MouseInventory;

public class TestUI extends InGameUI {

    public TestUI() {
        BaseItemElement item = new BaseItemElement(MouseInventory.mouseInventory.slots.get(0));
        item.x = -32;
        item.y = -32;
        item.width = 64;
        item.height = 64;
        item.repaint();
        uiElements.add(item);
    }

}

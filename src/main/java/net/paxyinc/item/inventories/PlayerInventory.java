package net.paxyinc.item.inventories;

import net.paxyinc.item.Item;
import net.paxyinc.item.ItemInventory;
import net.paxyinc.item.ItemSlot;

import java.util.List;

public class PlayerInventory extends ItemInventory {

    public static final int HOTBAR = 0;
    public static final int INVENTORY = HOTBAR + 9;
    public static final int ARMOR = INVENTORY + 3 * 9;
    public static final int SLOT_COUNT = ARMOR + 4;

    public int selectedHotbarSlot = 0;

    public PlayerInventory() {
        super(SLOT_COUNT);
    }

    public void pickBlock(Item item) {
        ItemSlot selected = getSelectedItem();
        List<ItemSlot> allItemSlots = allSlotsForItem(item).toList();
        if(!allItemSlots.isEmpty()) {
            ItemSlot inHotbar = getHotbarItemSlot(item);
            if(inHotbar != null) {
                setSelectedItem(inHotbar);
            } else {
                swapSlots(selected, allItemSlots.get(0));
            }
        }
    }

    public ItemSlot getHotbarItemSlot(Item item) {
        for(ItemSlot slot : slots.subList(HOTBAR, HOTBAR + 9)) {
            if(slot.itemStack != null && slot.itemStack.item == item) return slot;
        }
        return null;
    }

    public ItemSlot getSelectedItem() {
        return slots.get(HOTBAR + selectedHotbarSlot);
    }

    public void setSelectedItem(ItemSlot slot) {
        selectedHotbarSlot = slot.slotId - HOTBAR;
    }

    public void moveSelectedHotbarSlot(int amount) {
        selectedHotbarSlot = Math.floorMod(selectedHotbarSlot + amount, 9);
    }

    public void setSelectedHotbarSlot(int selectedHotbarSlot) {
        this.selectedHotbarSlot = selectedHotbarSlot;
    }
}

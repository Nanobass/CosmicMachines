package net.paxyinc.machines.item;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItemInventory {

    // TODO finish this
    public static void swapSlots(ItemSlot slot1, ItemSlot slot2) {
        if(slot1.itemStack == null && slot2.itemStack == null) return;
        if(slot1.itemStack == null && slot2.itemStack != null) {

        }
        if(slot1.itemStack != null && slot2.itemStack == null) {

        }
        if(slot1.itemStack != null && slot2.itemStack != null) {

        }
        ItemStack stack = slot1.itemStack;
        slot1.itemStack = slot2.itemStack;
        slot2.itemStack = stack;
    }

    public final List<ItemSlot> slots;

    public IItemInventoryRenderer renderer;

    public ItemInventory(int numSlots) {
        slots = new ArrayList<>();
        for(int i = 0; i < numSlots; i++) {
            slots.add(new ItemSlot(this, i));
        }
    }

    public List<ItemSlot> allSlotsForItem(Item item) {
        return slots.stream().filter(slot -> slot.itemStack != null && slot.itemStack.item == item).sorted().toList();
    }

    public ItemSlot getFirstFreeSlot() {
        for(ItemSlot slot : slots) {
            if(slot.itemStack == null) return slot;
        }
        return null;
    }

    public boolean hasEnoughItems(Item item, int amount) {
        List<ItemSlot> allItemSlots = allSlotsForItem(item);
        int totalItemAvailable = allItemSlots.stream().mapToInt(slot -> slot.itemStack.amount).sum();
        return totalItemAvailable >= amount;
    }

    public boolean takeItemsFromSlot(ItemSlot slot, int amount) {
        ItemStack stack = slot.itemStack;
        if(stack != null) {
            if(stack.amount - amount == 0) slot.itemStack = null;
            if(amount <= stack.amount) {
                stack.amount -= amount;
                return true;
            }
        }
        return false;
    }

    public boolean takeItems(Item item, int amount) {
        List<ItemSlot> allItemSlots = allSlotsForItem(item);
        int totalItemAvailable = allItemSlots.stream().mapToInt(slot -> slot.itemStack.amount).sum();
        if(totalItemAvailable < amount) return false;
        for(ItemSlot slot : allItemSlots) {
            int toRemove = Math.min(slot.itemStack.amount, totalItemAvailable);
            totalItemAvailable -= toRemove;
            slot.itemStack.amount -= toRemove;
            if(slot.itemStack.amount == 0) slot.itemStack = null;
        }
        return true;
    }

    // TODO check if enough space here
    public boolean pickupItems(Item item, int amount) {
        List<ItemSlot> allItemSlots = allSlotsForItem(item);
        int totalItemsLeft = amount;
        for(ItemSlot slot : allItemSlots) {
            int toAdd = Math.min(slot.itemStack.max - slot.itemStack.amount, totalItemsLeft);
            totalItemsLeft -= toAdd;
            slot.itemStack.amount += toAdd;
        }
        while(totalItemsLeft > 0) {
            ItemSlot free = getFirstFreeSlot();
            int toAdd = Math.min(64, totalItemsLeft);
            free.itemStack = new ItemStack(item, toAdd, 64);
            totalItemsLeft -= toAdd;
        }
        return true;
    }

}

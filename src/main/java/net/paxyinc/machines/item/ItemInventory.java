package net.paxyinc.machines.item;

import dev.crmodders.flux.tags.Identifier;
import net.paxyinc.machines.item.lists.ItemList;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ItemInventory extends AbstractItemList {


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

    public IItemInventoryRenderer renderer;
    public final List<ItemSlot> slots;

    public ItemInventory(int numSlots) {
        slots = IntStream.range(0, numSlots).mapToObj(i -> new ItemSlot(this, i)).toList();
    }

    public Stream<ItemSlot> allSlotsForItem(Item item) {
        return slots.stream().filter(slot -> slot.itemStack != null && slot.itemStack.item == item).sorted();
    }

    public Stream<ItemSlot> allSlotsForTag(Identifier tag) {
        return slots.stream().filter(slot -> slot.itemStack != null && slot.itemStack.item.tags.contains(tag)).sorted();
    }

    public Stream<ItemSlot> allSlotsFree() {
        return slots.stream().filter(slot -> slot.itemStack == null);
    }

    private Stream<ItemStack> forItem(Item item) {
        return allSlotsForItem(item).map(slot -> slot.itemStack);
    }

    private Stream<ItemStack> forTag(Identifier tag) {
        return allSlotsForTag(tag).map(slot -> slot.itemStack);
    }

    @Override
    public boolean hasEnoughSpace(Item item, int amount) {
        return allSlotsForItem(item).mapToInt(slot -> slot.itemStack.max - slot.itemStack.amount).sum() + allSlotsFree().count() * item.maxStack >= amount;
    }

    @Override
    public boolean hasEnoughSpace(AbstractItemList list) {
        int freeSlots = (int) allSlotsFree().count();
        for(Item item : list) {
            int amount = list.amount(item);
            int free = allSlotsForItem(item).mapToInt(slot -> slot.itemStack.max - slot.itemStack.amount).sum();
            int slotsNeeded = (int) Math.ceil( (double) (amount - free) / (double) item.maxStack );
            freeSlots -= slotsNeeded;
            if(freeSlots < 0) return false;
        }
        return true;
    }

    @Override
    public AbstractItemList remove(Item item, int amount) {
        if(!hasEnough(item, amount)) return ItemList.empty();
        ItemList removed = new ItemList();
        for(ItemStack stack : forItem(item).toList()) {
            int toRemove = Math.min(amount, stack.amount);
            removed.put(remove(stack.item, toRemove));
            amount -= toRemove;
            if(amount == 0) return removed;
        }
        throw new RuntimeException("Item leak, report to author");
    }

    @Override
    public AbstractItemList remove(Identifier tag, int amount) {
        if(!hasEnough(tag, amount)) return ItemList.empty();
        ItemList removed = new ItemList();
        for(ItemStack stack : forTag(tag).toList()) {
            int toRemove = Math.min(amount, stack.amount);
            removed.put(remove(stack.item, toRemove));
            amount -= toRemove;
            if(amount == 0) return removed;
        }
        throw new RuntimeException("Item leak, report to author");
    }

    @Override
    public boolean put(Item item, int amount) {
        if(!hasEnoughSpace(item, amount)) return false;
        for(ItemStack stack : forItem(item).toList()) {
            int toAdd = Math.min(amount, stack.max - stack.amount);
            stack.amount += toAdd;
            amount -= toAdd;
            if(amount == 0) return true;
        }
        for(ItemSlot free : allSlotsFree().toList()) {
            int toAdd = Math.min(amount, item.maxStack);
            free.itemStack = new ItemStack(item, toAdd, item.maxStack);
            amount -= toAdd;
            if(amount == 0) return true;
        }
        throw new RuntimeException("Item leak, report to author");
    }

    @Override
    public boolean put(AbstractItemList list) {
        if(!hasEnoughSpace(list)) return false;
        for(Item item : list) {
            int amount = list.amount(item);
            this.put(item, amount);
        }
        return true;
    }

    @Override
    public boolean take(AbstractItemList list) {
        if(!hasEnoughSpace(list)) return false;
        for(Item item : list) {
            int amount = list.amount(item);
            this.put(item, amount);
            list.remove(item, amount);
        }
        return true;
    }

    @Override
    public int amount(Item item) {
        return allSlotsForItem(item).mapToInt(slot -> slot.itemStack.amount).sum();
    }

    @Override
    public int amount(Identifier tag) {
        return allSlotsForTag(tag).mapToInt(slot -> slot.itemStack.amount).sum();
    }

    @Override
    public Iterator<Item> iterator() {
        Set<Item> items = new HashSet<>();
        slots.stream().filter(slot -> slot.itemStack != null).forEach(slot -> items.add(slot.itemStack.item));
        return items.iterator();
    }
}

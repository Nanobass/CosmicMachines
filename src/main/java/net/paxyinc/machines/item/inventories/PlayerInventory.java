package net.paxyinc.machines.item.inventories;

import finalforeach.cosmicreach.blocks.BlockState;
import net.paxyinc.machines.item.*;
import net.paxyinc.machines.item.renderers.PlayerInventoryRenderer;

public class PlayerInventory extends ItemInventory {

    public static PlayerInventory inventory = new PlayerInventory();

    public static final int HOTBAR = 0;
    public static final int INVENTORY = HOTBAR + 9;
    public static final int ARMOR = INVENTORY + 3 * 9;
    public static final int SLOT_COUNT = ARMOR + 4;

    public boolean renderInventory = false;
    public boolean renderHotbar = true;
    public int hotbarIndex = 0;

    public PlayerInventory() {
        super(SLOT_COUNT);
        renderer = new PlayerInventoryRenderer(this);
    }

    public void pickBlock(BlockState state) {

    }

    public boolean takeBlocks(ItemSlot slot, int amount) {
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

    public boolean pickupBlocks(Item item, int amount) {
        for(int i = 0; i < slots.size(); i++) {
            ItemStack stack = slots.get(i).itemStack;
            if(stack == null) continue;
            if(stack.item == item && stack.amount + amount <= stack.max) {
                stack.amount += amount;
                return true;
            }
        }
        for(int i = 0; i < slots.size(); i++) {
            if(slots.get(i).itemStack == null) {
                ItemStack stack = new ItemStack(item, amount, 64);
                slots.get(i).itemStack = stack;
                return true;
            }
        }
        return false;
    }

    public ItemSlot getSelectedItem() {
        return slots.get(HOTBAR + hotbarIndex);
    }

}

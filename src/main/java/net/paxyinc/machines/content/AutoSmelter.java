package net.paxyinc.machines.content;

import finalforeach.cosmicreach.entities.Player;
import net.paxyinc.entities.ItemEntity;
import net.paxyinc.item.IEntityInventory;
import net.paxyinc.item.Item;
import net.paxyinc.item.ItemInventory;
import net.paxyinc.item.ItemSlot;
import net.paxyinc.item.inventories.FurnaceInventory;
import net.paxyinc.item.renderers.FurnaceInventoryRenderer;
import net.paxyinc.machines.BaseMachine;
import net.paxyinc.ui.BlockInventoryUI;
import net.paxyinc.ui.UI2;
import net.querz.nbt.tag.CompoundTag;

public class AutoSmelter extends BaseMachine implements IEntityInventory {

    public AutoSmelter() {
        super(40000, Integer.MAX_VALUE, Integer.MAX_VALUE, 40);
    }

    public FurnaceInventory inventory = new FurnaceInventory();
    public ItemSlot input = inventory.slots.get(0), fuel = inventory.slots.get(1), output = inventory.slots.get(2);

    public BlockInventoryUI ui = new BlockInventoryUI(new FurnaceInventoryRenderer(inventory));

    boolean powerState = false;
    boolean lastPowerState = false;

    @Override
    public void read(CompoundTag nbt) {
        super.read(nbt);
        inventory.read(nbt);
    }

    @Override
    public void write(CompoundTag nbt) {
        super.write(nbt);
        inventory.write(nbt);
    }

    @Override
    public void onPoweredTick() {
        super.onPoweredTick();
        powerState = true;

        Item inputItem = input.getItem();
        Item fuelItem = fuel.getItem();
        if(inputItem != null && fuelItem != null) {
            boolean isFuel = fuelItem.tags.contains(Item.TAG_FUEL);
        }

    }

    @Override
    public void onTick() {
        powerState = false;
        super.onTick();
        if(powerState && !lastPowerState) {
            position.setBlockState(block.blockStates.get("on"));
            position.flagTouchingChunksForRemeshing(position.chunk.region.zone, true);
            lastPowerState = powerState;
        }
        if(!powerState && lastPowerState) {
            position.setBlockState(block.blockStates.get("off"));
            position.flagTouchingChunksForRemeshing(position.chunk.region.zone, true);
            lastPowerState = powerState;
        }
    }

    @Override
    public void onInteract(Player player) {
        UI2.openBlockUI(ui);
        UI2.openPlayerInventory(true);
    }

    @Override
    public void onDestroy() {
        ItemEntity.dropItems(position.chunk.region.zone, position, inventory);
    }

    @Override
    public ItemInventory accessInventory() {
        return inventory;
    }

}

package net.paxyinc.machines.content.machines;

import dev.crmodders.flux.api.block.IModBlock;
import dev.crmodders.flux.api.generators.BasicCubeModelGenerator;
import dev.crmodders.flux.api.generators.BlockGenerator;
import dev.crmodders.flux.api.generators.BlockModelGenerator;
import dev.crmodders.flux.api.resource.ResourceLocation;
import dev.crmodders.flux.tags.Identifier;
import finalforeach.cosmicreach.blocks.Block;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.entities.Player;
import finalforeach.cosmicreach.world.Zone;
import net.paxyinc.machines.MachineMod;
import net.paxyinc.machines.content.blocks.AutoSmelterBlock;
import net.paxyinc.machines.entities.ItemEntity;
import net.paxyinc.machines.item.Item;
import net.paxyinc.machines.item.inventories.FurnaceInventory;
import net.paxyinc.machines.machines.BaseMachine;
import net.paxyinc.machines.item.IEntityInventory;
import net.paxyinc.machines.item.ItemInventory;
import net.paxyinc.machines.item.ItemSlot;
import net.paxyinc.machines.ui.BlockInventoryUI;
import net.paxyinc.machines.ui.UI2;
import net.querz.nbt.tag.CompoundTag;

import java.util.List;

public class AutoSmelter extends BaseMachine implements IEntityInventory {

    public AutoSmelter() {
        super(40000, Integer.MAX_VALUE, Integer.MAX_VALUE, 40);
    }

    public FurnaceInventory inventory = new FurnaceInventory();
    public ItemSlot input = inventory.slots.get(0), fuel = inventory.slots.get(1), output = inventory.slots.get(2);

    public BlockInventoryUI ui = new BlockInventoryUI(inventory);

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
    public void onPoweredTick(Zone zone) {
        super.onPoweredTick(zone);
        powerState = true;

        Item inputItem = input.getItem();
        Item fuelItem = fuel.getItem();
        if(inputItem != null && fuelItem != null) {
            boolean isFuel = fuelItem.tags.contains(Item.TAG_FUEL);
        }

    }

    @Override
    public void onTick(Zone zone) {
        powerState = false;
        super.onTick(zone);
        BlockPosition position = getPosition(zone);
        Block block = position.getBlockState().getBlock();
        if(powerState && !lastPowerState) {
            position.setBlockState(block.blockStates.get("on"));
            position.flagTouchingChunksForRemeshing(zone, true);
            lastPowerState = powerState;
        }
        if(!powerState && lastPowerState) {
            position.setBlockState(block.blockStates.get("off"));
            position.flagTouchingChunksForRemeshing(zone, true);
            lastPowerState = powerState;
        }
    }

    @Override
    public void onInteract(Zone zone, Player player) {
        UI2.openBlockUI(ui);
    }

    @Override
    public void onDestroy(Zone zone) {
        ItemEntity.dropItems(zone, getPosition(zone), inventory);
    }

    @Override
    public ItemInventory accessInventory() {
        return inventory;
    }

}

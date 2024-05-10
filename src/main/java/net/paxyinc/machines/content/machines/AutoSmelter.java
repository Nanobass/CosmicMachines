package net.paxyinc.machines.content.machines;

import dev.crmodders.flux.api.block.IModBlock;
import dev.crmodders.flux.api.generators.BasicCubeModelGenerator;
import dev.crmodders.flux.api.generators.BlockGenerator;
import dev.crmodders.flux.api.generators.BlockModelGenerator;
import dev.crmodders.flux.api.resource.ResourceLocation;
import dev.crmodders.flux.tags.Identifier;
import finalforeach.cosmicreach.blocks.Block;
import finalforeach.cosmicreach.entities.Player;
import finalforeach.cosmicreach.world.Zone;
import net.paxyinc.machines.MachineMod;
import net.paxyinc.machines.content.blocks.AutoSmelterBlock;
import net.paxyinc.machines.item.Item;
import net.paxyinc.machines.item.inventories.FurnaceInventory;
import net.paxyinc.machines.machines.BaseMachine;
import net.paxyinc.machines.item.IEntityInventory;
import net.paxyinc.machines.item.ItemInventory;
import net.paxyinc.machines.item.ItemSlot;
import net.paxyinc.machines.ui.BlockInventoryUI;
import net.paxyinc.machines.ui.UI2;

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
        Block block = Block.blocksByStringId.get(AutoSmelterBlock.BLOCK_ID.toString());
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
    public ItemInventory accessInventory() {
        return inventory;
    }

}

package org.example.mod.blocks;

import dev.crmodders.flux.api.v5.block.IModBlock;
import dev.crmodders.flux.api.v5.generators.BlockGenerator;
import dev.crmodders.flux.api.v5.generators.data.blockevent.BlockEventType;
import dev.crmodders.flux.api.v5.resource.ResourceLocation;
import dev.crmodders.flux.logging.LogWrapper;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.entities.Player;
import finalforeach.cosmicreach.world.Zone;
import org.example.mod.ExampleMod;

public class ResourceExampleBlock implements IModBlock {

    private static String TAG = ExampleMod.createTag("ResourceExampleBlock");

    BlockGenerator blockGenerator;

    public ResourceExampleBlock() {
        // Creates a generator based off of a block in a specified resource location ("assets/{MOD_ID}/blocks/{NAME.json}")
        blockGenerator = BlockGenerator.createResourceDrivenGenerator(new ResourceLocation(ExampleMod.MOD_ID, "diamond_block.json"));
        
        // Overrides specific block event from parent event to prevent the block from being broken
        blockGenerator.overrideEvent(BlockEventType.OnBreak, false);
    }

    @Override
    public void onInteract(Zone zone, Player player, BlockState blockState, BlockPosition position) {
        LogWrapper.info("%s: Who touched my Diamonds?".formatted(TAG));
    }

    @Override
    public void onPlace(Zone zone, Player player, BlockState blockState, BlockPosition position) {
        LogWrapper.info("%s: Block has been placed at (%d, %d, %d)".formatted(TAG, position.localX, position.localY, position.localZ));
    }

    @Override
    public void onBreak(Zone zone, Player player, BlockState blockState, BlockPosition position) {
        LogWrapper.info("%s: NO DONT BREAK THE DIAMONDS WITH YOUR FIST, YOU MAY LOSE THEM!".formatted(TAG));
    }

    @Override
    public BlockGenerator getGenerator() {
        return blockGenerator;
    }
}
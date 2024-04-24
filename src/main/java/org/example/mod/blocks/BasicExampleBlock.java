package org.example.mod.blocks;

import com.badlogic.gdx.utils.Queue;
import dev.crmodders.flux.api.v5.block.IModBlock;
import dev.crmodders.flux.api.v5.generators.BlockGenerator;
import dev.crmodders.flux.api.v5.generators.data.blockevent.BlockEventType;
import dev.crmodders.flux.logging.LogWrapper;
import finalforeach.cosmicreach.blocks.Block;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.entities.Player;
import finalforeach.cosmicreach.world.BlockSetter;
import finalforeach.cosmicreach.world.Zone;
import org.example.mod.ExampleMod;

public class BasicExampleBlock implements IModBlock {

    private static String TAG = ExampleMod.createTag("BasicExampleBlock");

    BlockGenerator blockGenerator;

    public BasicExampleBlock() {
        // Creates a generator for a block, if this block has a json with the same registry id in assets/MOD_ID/blocks/BLOCK/NAME.json and if id does it will use that as the base block
        blockGenerator = BlockGenerator.createGenerator();

        // Overrides specific block event from parent event to prevent the block from being broken
        blockGenerator.overrideEvent(BlockEventType.OnBreak, true);

        /* This allows you to inject custom triggers into any blockstate
        without you having to make a hacky way inside the IModBlock methods */
        blockGenerator.addTriggerMultiStateTrigger(
                BlockEventType.OnBreak,
                this::onBreak
        );
    }

    @Override
    public void onInteract(Zone zone, Player player, BlockState blockState, BlockPosition position) {
        LogWrapper.info("%s: I have been touched, time to transform DIRT STYLE".formatted(TAG));
        BlockSetter.replaceBlock(zone, Block.DIRT.getDefaultBlockState(), position, new Queue<>());
    }

    @Override
    public void onPlace(Zone zone, Player player, BlockState blockState, BlockPosition position) {
        LogWrapper.info("%s: Block has been placed at (%d, %d, %d)".formatted(TAG, position.localX, position.localY, position.localZ));
    }

    @Override
    public void onBreak(Zone zone, Player player, BlockState blockState, BlockPosition position) {
        LogWrapper.info("%s: Nice try Player this block is unbreakable".formatted(TAG));
    }

    @Override
    public BlockGenerator getGenerator() {
        return blockGenerator;
    }
}

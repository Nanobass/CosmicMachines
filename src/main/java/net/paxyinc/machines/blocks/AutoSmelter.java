package net.paxyinc.machines.blocks;

import dev.crmodders.flux.api.block.IModBlock;
import dev.crmodders.flux.api.generators.BasicCubeModelGenerator;
import dev.crmodders.flux.api.generators.BlockGenerator;
import dev.crmodders.flux.api.generators.BlockModelGenerator;
import dev.crmodders.flux.api.resource.ResourceLocation;
import dev.crmodders.flux.menus.BasicMenu;
import dev.crmodders.flux.tags.Identifier;
import finalforeach.cosmicreach.blocks.Block;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.entities.Player;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.world.Zone;
import net.paxyinc.machines.energy.base.BaseMachine;

import java.util.List;

public class AutoSmelter extends BaseMachine implements IModBlock {
    public static final Identifier BLOCK_ID = new Identifier("machines", "smelter");
    public static final String BLOCK_NAME = "auto_smelter";
    public static final ResourceLocation TEXTURE_SIDE = new ResourceLocation("fluxapi", "textures/blocks/flux_furnace_side.png");
    public static final ResourceLocation TEXTURE_FRONT_ON = new ResourceLocation("fluxapi", "textures/blocks/flux_furnace_front_on.png");
    public static final ResourceLocation TEXTURE_FRONT_OFF = new ResourceLocation("fluxapi", "textures/blocks/flux_furnace_front_off.png");

    public AutoSmelter() {
        super(40000, Integer.MAX_VALUE, Integer.MAX_VALUE, 40);
    }

    boolean powerState = false;
    boolean lastPowerState = false;

    @Override
    public void onPoweredTick(Zone zone) {
        super.onPoweredTick(zone);
        powerState = true;
    }

    @Override
    public void onTick(Zone zone) {
        powerState = false;
        super.onTick(zone);
        Block block = Block.blocksByStringId.get(BLOCK_ID.toString());
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

    private static class FurnaceUI extends BasicMenu {
        public FurnaceUI(GameState last) {
            super(last);
            addBackButton();
        }
    }

    public void onInteract(Zone zone, Player player, BlockState blockState, BlockPosition position) {
        GameState.switchToGameState(new FurnaceUI(GameState.currentGameState));
    }

    public BlockGenerator getBlockGenerator() {
        BlockGenerator generator = new BlockGenerator(BLOCK_ID, BLOCK_NAME);
        BlockGenerator.State on = generator.createBlockState("on", "on", true);
        on.lightLevelRed = on.lightLevelGreen = on.lightLevelBlue = 15;
        on.catalogHidden = true;
        BlockGenerator.State off = generator.createBlockState("off", "off", true);
        off.lightLevelRed = off.lightLevelGreen = off.lightLevelBlue = 0;
        return generator;
    }

    public List<BlockModelGenerator> getBlockModelGenerators(Identifier blockId) {
        BlockModelGenerator on = new BasicCubeModelGenerator(blockId, "on", false, TEXTURE_SIDE, TEXTURE_FRONT_ON);
        BlockModelGenerator off = new BasicCubeModelGenerator(blockId, "off", false, TEXTURE_SIDE, TEXTURE_FRONT_OFF);
        return List.of(on, off);
    }
}

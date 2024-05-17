package net.paxyinc.machines.content.blocks;

import dev.crmodders.flux.api.generators.BasicCubeModelGenerator;
import dev.crmodders.flux.api.generators.BlockGenerator;
import dev.crmodders.flux.api.generators.BlockModelGenerator;
import dev.crmodders.flux.api.resource.ResourceLocation;
import dev.crmodders.flux.tags.Identifier;
import net.paxyinc.MachineMod;
import net.paxyinc.entities.FunctionalBlock;
import net.paxyinc.entities.IFunctionalModBlock;
import net.paxyinc.machines.content.AutoSmelter;

import java.util.List;

public class AutoSmelterBlock implements IFunctionalModBlock {
    public static final Identifier BLOCK_ID = new Identifier(MachineMod.MOD_ID, "smelter");
    public static final String BLOCK_NAME = "auto_smelter";
    public static final ResourceLocation TEXTURE_SIDE = new ResourceLocation("fluxapi", "textures/blocks/flux_furnace_side.png");
    public static final ResourceLocation TEXTURE_FRONT_ON = new ResourceLocation("fluxapi", "textures/blocks/flux_furnace_front_on.png");
    public static final ResourceLocation TEXTURE_FRONT_OFF = new ResourceLocation("fluxapi", "textures/blocks/flux_furnace_front_off.png");

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

    @Override
    public FunctionalBlock createFunctionalBlock() {
        return new AutoSmelter();
    }
}
package net.paxyinc.machines.content.blocks;

import dev.crmodders.flux.api.generators.BlockGenerator;
import dev.crmodders.flux.api.generators.BlockModelGenerator;
import dev.crmodders.flux.api.resource.ResourceLocation;
import dev.crmodders.flux.tags.Identifier;
import net.paxyinc.MachineMod;
import net.paxyinc.entities.FunctionalBlock;
import net.paxyinc.entities.IFunctionalModBlock;
import net.paxyinc.machines.content.Cable;
import net.paxyinc.machines.content.models.CableModelGenerator;

import java.util.List;

public class CableBlock implements IFunctionalModBlock {

    public static final Identifier BLOCK_ID = new Identifier(MachineMod.MOD_ID, "cable");
    public static final String BLOCK_NAME = "cable";
    public static final ResourceLocation CABLE_TEXTURE = new ResourceLocation(MachineMod.MOD_ID, "textures/blocks/cable.png");

    @Override
    public BlockGenerator getBlockGenerator() {
        return CableModelGenerator.getAllBlockStates(BLOCK_ID, BLOCK_NAME);
    }

    @Override
    public List<BlockModelGenerator> getBlockModelGenerators(Identifier blockId) {
        return CableModelGenerator.getAllGenerators(blockId, CABLE_TEXTURE);
    }

    @Override
    public FunctionalBlock createFunctionalBlock() {
        return new Cable();
    }
}


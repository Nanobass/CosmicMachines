package net.paxyinc.machines.content.blocks;

import dev.crmodders.flux.api.block.IModBlock;
import dev.crmodders.flux.api.generators.BlockGenerator;
import dev.crmodders.flux.api.generators.BlockModelGenerator;
import dev.crmodders.flux.api.resource.ResourceLocation;
import dev.crmodders.flux.tags.Identifier;
import net.paxyinc.machines.MachineMod;
import net.paxyinc.machines.content.machines.Cable;
import net.paxyinc.machines.content.models.CableModelGenerator;
import net.paxyinc.machines.entities.IModTileEntity;
import net.paxyinc.machines.entities.TileEntity;

import java.util.List;
import java.util.function.Supplier;

public class CableBlock implements IModTileEntity {

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
    public Supplier<TileEntity> getTileEntityFactory() {
        return Cable::new;
    }
}


package net.paxyinc.machines.content.machines;

import dev.crmodders.flux.api.block.IModBlock;
import dev.crmodders.flux.api.generators.BlockGenerator;
import dev.crmodders.flux.api.generators.BlockModelGenerator;
import dev.crmodders.flux.api.resource.ResourceLocation;
import dev.crmodders.flux.tags.Identifier;
import net.paxyinc.machines.MachineMod;
import net.paxyinc.machines.content.blocks.CableBlock;
import net.paxyinc.machines.content.models.CableModelGenerator;
import net.paxyinc.machines.machines.BaseCable;

import java.util.List;

public class Cable extends BaseCable {

    public Cable() {
        super(256, 1024);
    }

}
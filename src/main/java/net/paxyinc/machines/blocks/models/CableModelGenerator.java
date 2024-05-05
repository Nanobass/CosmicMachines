package net.paxyinc.machines.blocks.models;

import dev.crmodders.flux.api.generators.BlockGenerator;
import dev.crmodders.flux.api.generators.BlockModelGenerator;
import dev.crmodders.flux.api.resource.ResourceLocation;
import dev.crmodders.flux.tags.Identifier;
import finalforeach.cosmicreach.constants.Direction;
import net.paxyinc.machines.util.DirectionUtil;

import java.util.ArrayList;
import java.util.List;

import static net.paxyinc.machines.util.DirectionUtil.ALL_DIRECTION_NAMES;

public class CableModelGenerator extends BlockModelGenerator {

    public static BlockGenerator getAllBlockStates(Identifier blockId, String blockName) {
        BlockGenerator generator = new BlockGenerator(blockId, blockName);
        for(String name : ALL_DIRECTION_NAMES.values()) {
            BlockGenerator.State state = generator.createBlockState(name, name, true);
            state.isOpaque = false;
            state.lightAttenuation = 1;
            state.catalogHidden = true;
        }
        generator.createBlockState("default", ALL_DIRECTION_NAMES.get(63), true);
        return generator;
    }

    public static List<BlockModelGenerator> getAllGenerators(Identifier blockId, ResourceLocation texture) {
        List<BlockModelGenerator> cableModelGenerators = new ArrayList<>();
        for(int mask : ALL_DIRECTION_NAMES.keySet()) {
            CableModelGenerator generator = new CableModelGenerator(blockId, mask);
            generator.createTexture("all", texture);
            cableModelGenerators.add(generator);
        }
        return cableModelGenerators;
    }

    public CableModelGenerator(Identifier blockId, int mask) {
        super(blockId, ALL_DIRECTION_NAMES.get(mask));

        Cuboid core = createCuboid(5, 5, 5, 11, 11, 11, "all");
        core.setUVs(5, 0, 11, 6);
        core.setCullFace(false);
        core.setAmbientOcclusion(false);

        DirectionUtil.directions(mask).forEach(face -> {
            Cuboid cuboid = switch (face) {
                case NEG_X -> createCuboid(0, 5, 5, 5, 11, 11, "all");
                case POS_X -> createCuboid(11, 5, 5, 16, 11, 11, "all");
                case NEG_Y -> createCuboid(5, 0, 5, 11, 5, 11, "all");
                case POS_Y -> createCuboid(5, 11, 5, 11, 16, 11, "all");
                case NEG_Z -> createCuboid(5, 5, 0, 11, 11, 5, "all");
                case POS_Z -> createCuboid(5, 5, 11, 11, 11, 16, "all");
            };

            cuboid.setCullFace(false);
            cuboid.setAmbientOcclusion(false);
            cuboid.face(face).cullFace = true;
            cuboid.face(face).setUVs(0, 10, 6, 16);

            if(face.isYAxis()) cuboid.setUVRotation(90);
            else if(face.isZAxis()) cuboid.face(Direction.POS_Y).uvRotation = cuboid.face(Direction.NEG_Y).uvRotation = 90;
            if(face.getXOffset() < 0 || face.getYOffset() < 0 || face.getZOffset() < 0) cuboid.setUVs(0, 0, 5, 6);
            else cuboid.setUVs(11, 0, 16, 6);

        });
    }

}
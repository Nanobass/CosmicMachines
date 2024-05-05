package net.paxyinc.machines.util;

import finalforeach.cosmicreach.blocks.Block;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.constants.AdjacentBitmask;
import finalforeach.cosmicreach.constants.Direction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DirectionUtil {

    public static final Map<Integer, String> ALL_DIRECTION_NAMES = new HashMap<>();
    public static final Map<String, Integer> ALL_DIRECTION_NAMES_INVERSE = new HashMap<>();

    static {
        for(int mask = 0; mask < 64; mask++) {
            StringBuilder name = new StringBuilder();
            if(isBitSet(mask, AdjacentBitmask.NEG_X)) name.append("negX").append("_");
            if(isBitSet(mask, AdjacentBitmask.POS_X)) name.append("posX").append("_");
            if(isBitSet(mask, AdjacentBitmask.NEG_Y)) name.append("negY").append("_");
            if(isBitSet(mask, AdjacentBitmask.POS_Y)) name.append("posY").append("_");
            if(isBitSet(mask, AdjacentBitmask.NEG_Z)) name.append("negZ").append("_");
            if(isBitSet(mask, AdjacentBitmask.POS_Z)) name.append("posZ").append("_");
            if(!name.isEmpty()) name.deleteCharAt(name.length() - 1);
            ALL_DIRECTION_NAMES.put(mask, name.toString());
            ALL_DIRECTION_NAMES_INVERSE.put(name.toString(), mask);
        }
    }

    public static int getBlockMask(BlockState block) {
        return ALL_DIRECTION_NAMES_INVERSE.get(block.stringId);
    }

    public static BlockState getBlockState(Block block, int mask) {
        return block.blockStates.get(ALL_DIRECTION_NAMES.get(mask));
    }

    public static Direction opposite(Direction face) {
        return switch (face) {
            case NEG_X -> Direction.POS_X;
            case POS_X -> Direction.NEG_X;
            case NEG_Y -> Direction.POS_Y;
            case POS_Y -> Direction.NEG_Y;
            case NEG_Z -> Direction.POS_Z;
            case POS_Z -> Direction.NEG_Z;
        };
    }

    public static int mask(Direction face) {
        return switch (face) {
            case NEG_X -> AdjacentBitmask.NEG_X;
            case POS_X -> AdjacentBitmask.POS_X;
            case NEG_Y -> AdjacentBitmask.NEG_Y;
            case POS_Y -> AdjacentBitmask.POS_Y;
            case NEG_Z -> AdjacentBitmask.NEG_Z;
            case POS_Z -> AdjacentBitmask.POS_Z;
        };
    }

    public static Direction direction(int mask) {
        return switch (mask) {
            case AdjacentBitmask.NEG_X -> Direction.POS_X;
            case AdjacentBitmask.POS_X -> Direction.NEG_X;
            case AdjacentBitmask.NEG_Y -> Direction.POS_Y;
            case AdjacentBitmask.POS_Y -> Direction.NEG_Y;
            case AdjacentBitmask.NEG_Z -> Direction.POS_Z;
            case AdjacentBitmask.POS_Z -> Direction.NEG_Z;
            default -> null;
        };
    }

    public static List<Direction> directions(int mask) {
        List<Direction> faces = new ArrayList<>(6);
        if (isBitSet(mask, AdjacentBitmask.NEG_X)) faces.add(Direction.NEG_X);
        if (isBitSet(mask, AdjacentBitmask.POS_X)) faces.add(Direction.POS_X);
        if (isBitSet(mask, AdjacentBitmask.NEG_Y)) faces.add(Direction.NEG_Y);
        if (isBitSet(mask, AdjacentBitmask.POS_Y)) faces.add(Direction.POS_Y);
        if (isBitSet(mask, AdjacentBitmask.NEG_Z)) faces.add(Direction.NEG_Z);
        if (isBitSet(mask, AdjacentBitmask.POS_Z)) faces.add(Direction.POS_Z);
        return faces;
    }

    public static int mask(Direction... faces) {
        int mask = 0;
        for(Direction face : faces) mask |= mask(face);
        return mask;
    }

    public static boolean isBitSet(int bits, int mask) {
        return (bits & mask) == mask;
    }

    public static int mask(Iterable<Direction> faces) {
        int mask = 0;
        for(Direction face : faces) mask |= mask(face);
        return mask;
    }

}

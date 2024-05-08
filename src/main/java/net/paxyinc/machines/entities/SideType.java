package net.paxyinc.machines.entities;

import finalforeach.cosmicreach.constants.Direction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum SideType {

    Disabled(false, false), Input(true, false), Output(false, true), Combined(true, true);

    public final boolean input, output;

    SideType(boolean input, boolean output) {
        this.input = input;
        this.output = output;
    }

    public static Map<Direction, SideType> createSideTypes(SideType type) {
        Map<Direction, SideType> energySides = new HashMap<>();
        Arrays.stream(Direction.values()).forEach(face -> energySides.put(face, type));
        return energySides;
    }

}

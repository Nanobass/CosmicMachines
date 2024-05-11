package net.paxyinc.machines.util;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import finalforeach.cosmicreach.blocks.BlockPosition;
import net.querz.nbt.tag.*;
import org.checkerframework.checker.units.qual.C;

import java.util.List;

public class NbtUtil {

    public static Tag<?> serializeVector3(Vector3 v) {
        ListTag<FloatTag> floats = new ListTag<>(FloatTag.class);
        floats.addFloat(v.x);
        floats.addFloat(v.y);
        floats.addFloat(v.z);
        return floats;
    }

    public static Vector3 deserializeVector3(Tag<?> tag) {
        if(tag instanceof ListTag<?> list) {
            ListTag<FloatTag> floats = list.asFloatTagList();
            FloatTag x = floats.get(0);
            FloatTag y = floats.get(1);
            FloatTag z = floats.get(2);
            return new Vector3(x.asFloat(), y.asFloat(), z.asFloat());
        } else {
            throw new RuntimeException("Unknown Type");
        }
    }

    public static Tag<?> serializeBoundingBox(BoundingBox bb) {
        CompoundTag compound = new CompoundTag();
        compound.put("min", serializeVector3(bb.min));
        compound.put("max", serializeVector3(bb.max));
        return compound;
    }

    public static BoundingBox deserializeBoundingBox(Tag<?> tag) {
        if(tag instanceof CompoundTag compound) {
            BoundingBox bb = new BoundingBox();
            bb.min.set(deserializeVector3(compound.get("min")));
            bb.max.set(deserializeVector3(compound.get("max")));
            bb.update();
            return bb;
        } else {
            throw new RuntimeException("Unknown Type");
        }
    }

}

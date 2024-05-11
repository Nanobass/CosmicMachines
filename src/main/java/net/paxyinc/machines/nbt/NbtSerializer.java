package net.paxyinc.machines.nbt;

import net.querz.nbt.io.NBTInput;
import net.querz.nbt.io.NBTOutput;
import net.querz.nbt.io.NBTSerializer;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.Tag;
import org.checkerframework.checker.units.qual.C;

import java.lang.reflect.InvocationTargetException;

public class NbtSerializer {

    public static <T extends NbtSerializable> CompoundTag write(T object) {
        Class<? extends NbtSerializable> clazz = object.getClass();
        CompoundTag outputTag = new CompoundTag();
        outputTag.putString("class", clazz.getName());
        object.write(outputTag);
        return outputTag;
    }

    public static <T extends NbtSerializable> T read(CompoundTag tag) throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException {
        String className = tag.getString("class");
        Class<? extends NbtSerializable> clazz = (Class<? extends NbtSerializable>) Class.forName(className);
        T object = (T) clazz.getDeclaredConstructor().newInstance();
        object.read(tag);
        return object;
    }

}

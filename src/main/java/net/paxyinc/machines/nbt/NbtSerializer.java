package net.paxyinc.machines.nbt;

import net.querz.nbt.tag.CompoundTag;

import java.lang.reflect.InvocationTargetException;

public class NbtSerializer {

    public static <T extends NbtSerializable> CompoundTag write(T object) {
        Class<? extends NbtSerializable> clazz = object.getClass();
        CompoundTag outputTag = new CompoundTag();
        outputTag.putString("class", clazz.getName());
        object.write(outputTag);
        return outputTag;
    }

    public static <T extends NbtSerializable> T read(CompoundTag tag) {
        try {
            String className = tag.getString("class");
            Class<? extends NbtSerializable> clazz = (Class<? extends NbtSerializable>) Class.forName(className);
            T object = (T) clazz.getDeclaredConstructor().newInstance();
            object.read(tag);
            return object;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Class Not Found", e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("No 0-Arg Constructor", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Constructor not Accessible", e);
        } catch (InvocationTargetException | InstantiationException ignored) {
            return null;
        }
    }

}

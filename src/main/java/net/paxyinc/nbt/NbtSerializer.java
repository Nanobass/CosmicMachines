package net.paxyinc.nbt;

import dev.crmodders.flux.tags.Identifier;
import net.querz.nbt.tag.CompoundTag;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;

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

    public static <T extends NbtSerializable & Identifiable> CompoundTag writeIdentifiable(T object) {
        Identifier id = object.id();
        CompoundTag outputTag = new CompoundTag();
        outputTag.putString("objectId", id.toString());
        object.write(outputTag);
        return outputTag;
    }

    public static <T extends NbtSerializable & Identifiable> T readIdentifiable(CompoundTag tag, Function<Identifier, T> producer) {
        Identifier id = Identifier.fromString(tag.getString("objectId"));
        T object = producer.apply(id);
        object.read(tag);
        return object;
    }


}

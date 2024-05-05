package net.paxyinc.machines.nbt;

import net.querz.nbt.tag.CompoundTag;

public interface NbtSerializable<T> {

    void read(CompoundTag nbt);

    void write(CompoundTag nbt);

}

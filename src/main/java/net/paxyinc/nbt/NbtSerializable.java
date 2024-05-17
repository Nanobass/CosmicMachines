package net.paxyinc.nbt;

import net.querz.nbt.tag.CompoundTag;

public interface NbtSerializable {

    void read(CompoundTag nbt);

    void write(CompoundTag nbt);

}

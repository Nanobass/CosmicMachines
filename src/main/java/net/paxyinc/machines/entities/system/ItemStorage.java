package net.paxyinc.machines.entities.system;

import net.paxyinc.machines.item.Item;
import net.paxyinc.machines.item.ItemInventory;
import net.paxyinc.machines.nbt.NbtSerializable;
import net.querz.nbt.tag.CompoundTag;

public class ItemStorage implements IItemStorage, NbtSerializable {

    public ItemInventory inventory;
    public int maxConsume;
    public int maxProduce;

    public ItemStorage(ItemInventory inventory, int maxConsume, int maxProduce) {
        this.inventory = inventory;
        this.maxConsume = maxConsume;
        this.maxProduce = maxProduce;
    }

    @Override
    public void read(CompoundTag nbt) {
        // TODO read inventory here
    }

    @Override
    public void write(CompoundTag nbt) {
        // TODO write inventory here
    }

    @Override
    public int consume(Item item, int amount, boolean simulate) {
        return 0;
    }

    @Override
    public int produce(Item item, int amount, boolean simulate) {
        return 0;
    }

    @Override
    public int available(Item item) {
        return 0;
    }

    @Override
    public int maximum(Item item) {
        return Integer.MAX_VALUE;
    }

}

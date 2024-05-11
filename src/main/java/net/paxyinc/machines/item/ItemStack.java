package net.paxyinc.machines.item;

import dev.crmodders.flux.tags.Identifier;
import net.paxyinc.machines.nbt.NbtSerializable;
import net.querz.nbt.tag.CompoundTag;

public class ItemStack implements NbtSerializable {
    public Item item;
    public int amount;
    public int max;

    public ItemStack() {

    }

    public ItemStack(Item item, int amount, int max) {
        this.item = item;
        this.amount = amount;
        this.max = max;
    }

    @Override
    public void write(CompoundTag nbt) {
        nbt.putString("itemId", item.itemId.toString());
        nbt.putInt("amount", amount);
        nbt.putInt("max", max);
    }

    @Override
    public void read(CompoundTag nbt) {
        Identifier itemId = Identifier.fromString(nbt.getString("itemId"));
        item = ItemRegistry.allItems.access().get(itemId);
        amount = nbt.getInt("amount");
        max = nbt.getInt("max");
    }

}
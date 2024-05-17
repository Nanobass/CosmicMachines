package net.paxyinc.item.lists;

import dev.crmodders.flux.tags.Identifier;
import net.paxyinc.item.AbstractItemList;
import net.paxyinc.item.Item;
import net.paxyinc.item.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class ItemList extends AbstractItemList {

    public static ItemList empty() {
        return new ItemList();
    }

    public static ItemList of(Item item, int amount) {
        ItemList list = new ItemList();
        list.put(item, amount);
        return list;
    }

    private List<ItemStack> stacks = new ArrayList<>();

    private Stream<ItemStack> forItem(Item item) {
        return stacks.stream().filter(stack -> stack.item == item);
    }

    private Stream<ItemStack> forTag(Identifier tag) {
        return stacks.stream().filter(stack -> stack.item.tags.contains(tag));
    }

    @Override
    public boolean hasEnoughSpace(Item item, int amount) {
        return true;
    }

    @Override
    public boolean hasEnoughSpace(AbstractItemList list) {
        return true;
    }

    @Override
    public ItemList remove(Item item, int amount) {
        if(!hasEnough(item, amount)) return empty();
        Optional<ItemStack> optional = forItem(item).findFirst();
        if(optional.isPresent()) {
            ItemStack stack = optional.get();
            stack.amount -= amount;
            if(stack.amount == 0) stacks.remove(stack);
        }
        return of(item, amount);
    }

    @Override
    public AbstractItemList remove(Identifier tag, int amount) {
        if(!hasEnough(tag, amount)) return empty();
        ItemList removed = new ItemList();
        for(ItemStack stack : forTag(tag).toList()) {
            int toRemove = Math.min(amount, stack.amount);
            removed.put(remove(stack.item, toRemove));
            amount -= toRemove;
            if(amount == 0) break;
        }
        return removed;
    }

    @Override
    public boolean put(Item item, int amount) {
        if(!hasEnoughSpace(item, amount)) return false;
        Optional<ItemStack> optional = forItem(item).findFirst();
        if(optional.isPresent()) {
            ItemStack stack = optional.get();
            stack.amount += amount;
        } else {
            ItemStack stack = new ItemStack(item, amount, item.maxStack);
            stacks.add(stack);
        }
        return true;
    }

    @Override
    public boolean put(AbstractItemList list) {
        if(!hasEnoughSpace(list)) return false;
        for(Item item : list) {
            int amount = list.amount(item);
            this.put(item, amount);
        }
        return true;
    }

    @Override
    public boolean take(AbstractItemList list) {
        if(!hasEnoughSpace(list)) return false;
        for(Item item : list) {
            int amount = list.amount(item);
            this.put(item, amount);
            list.remove(item, amount);
        }
        return true;
    }

    @Override
    public int amount(Item item) {
        return forItem(item).mapToInt(stack -> stack.amount).sum();
    }

    @Override
    public int amount(Identifier tag) {
        return forTag(tag).mapToInt(stack -> stack.amount).sum();
    }

    @Override
    public Iterator<Item> iterator() {
        return stacks.stream().map(stack -> stack.item).iterator();
    }

    @Override
    public void clear() {
        stacks.clear();
    }
}

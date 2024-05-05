package net.paxyinc.machines.item;

import dev.crmodders.flux.registry.FluxRegistries;
import dev.crmodders.flux.registry.registries.DynamicRegistry;
import dev.crmodders.flux.registry.registries.FreezingRegistry;

public class ItemRegistry {

    public static final DynamicRegistry<Item> allItems = DynamicRegistry.create();
    public static final FreezingRegistry<Runnable> ITEM_FINALIZERS = FreezingRegistry.create();

}

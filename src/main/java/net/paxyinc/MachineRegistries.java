package net.paxyinc;

import dev.crmodders.flux.api.factories.IFactory;
import dev.crmodders.flux.registry.registries.DynamicRegistry;
import dev.crmodders.flux.registry.registries.FreezingRegistry;
import net.paxyinc.entities.BetterEntity;
import net.paxyinc.item.Item;

public class MachineRegistries {

    public static final DynamicRegistry<Item> FLUIDS = DynamicRegistry.create();
    public static final FreezingRegistry<Runnable> FLUID_FINALIZERS = FreezingRegistry.create();

    public static final DynamicRegistry<Item> GASES = DynamicRegistry.create();
    public static final FreezingRegistry<Runnable> GAS_FINALIZERS = FreezingRegistry.create();

    public static final DynamicRegistry<Item> ITEMS = DynamicRegistry.create();
    public static final FreezingRegistry<Runnable> ITEM_FINALIZERS = FreezingRegistry.create();

    public static final DynamicRegistry<Item> TOOLS = DynamicRegistry.create();
    public static final FreezingRegistry<Runnable> TOOLS_FINALIZERS = FreezingRegistry.create();

    public static final DynamicRegistry<IFactory<BetterEntity>> ENTITY_FACTORIES = DynamicRegistry.create();

}

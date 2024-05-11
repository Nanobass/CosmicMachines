package net.paxyinc.machines.entities;

import dev.crmodders.flux.registry.registries.AccessableRegistry;
import dev.crmodders.flux.registry.registries.DynamicRegistry;

import java.util.function.Supplier;

public class TileEntityRegistry {
    public static final DynamicRegistry<Supplier<TileEntity>> BLOCK_TILE_ENTITY_FACTORIES = DynamicRegistry.create();
    public static final AccessableRegistry<Supplier<TileEntity>> BLOCK_TILE_ENTITY_FACTORIES_ACCESS = BLOCK_TILE_ENTITY_FACTORIES.access();
}

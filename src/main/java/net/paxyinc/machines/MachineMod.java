package net.paxyinc.machines;

import dev.crmodders.flux.api.block.IModBlock;
import dev.crmodders.flux.api.events.GameEvents;
import dev.crmodders.flux.registry.FluxRegistries;
import dev.crmodders.flux.registry.registries.AccessableRegistry;
import dev.crmodders.flux.tags.Identifier;
import dev.crmodders.flux.util.PrivUtils;
import finalforeach.cosmicreach.blocks.Block;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.ui.UI;
import net.fabricmc.api.ModInitializer;
import net.paxyinc.machines.blocks.AutoSmelter;
import net.paxyinc.machines.energy.base.BaseGenerator;
import net.paxyinc.machines.blocks.Cable;
import net.paxyinc.machines.entities.TileEntityManager;
import net.paxyinc.machines.item.Item;
import net.paxyinc.machines.item.ItemRegistry;
import net.paxyinc.machines.item.inventories.PlayerInventory;
import net.paxyinc.machines.item.items.BlockItem;

import java.util.HashMap;
import java.util.Map;

public class MachineMod implements ModInitializer {

    public static String MOD_ID = "cosmicmachines";
    public static Identifier MOD_NAME = new Identifier(MOD_ID, "Cosmic Machines");

    @Override
    public void onInitialize() {
        FluxRegistries.ON_PRE_INITIALIZE.register(MOD_NAME, this::onFluxPreInit);
        FluxRegistries.ON_INITIALIZE.register(MOD_NAME, this::onFluxInit);
        FluxRegistries.ON_POST_INITIALIZE.register(MOD_NAME, this::onFluxPostInit);
    }

    public void onFluxPreInit() {
        GameEvents.BEFORE_GAME_IS_TICKED.register(TileEntityManager.MANAGER);
        GameEvents.AFTER_BLOCK_IS_PLACED.register(TileEntityManager.MANAGER);
        GameEvents.BEFORE_BLOCK_IS_BROKEN.register(TileEntityManager.MANAGER);
    }

    public void onFluxInit() {
        FluxRegistries.BLOCK_FACTORIES.add(Cable::new);
        TileEntityManager.BLOCK_TILE_ENTITY_FACTORIES.register(Cable.BLOCK_ID, Cable::new);
        FluxRegistries.BLOCK_FACTORIES.add(AutoSmelter::new);
        TileEntityManager.BLOCK_TILE_ENTITY_FACTORIES.register(AutoSmelter.BLOCK_ID, AutoSmelter::new);
        TileEntityManager.BLOCK_TILE_ENTITY_FACTORIES.register(Identifier.fromString("base:asphalt"), () -> new BaseGenerator(40000, Integer.MAX_VALUE, Integer.MAX_VALUE, 40));
    }

    public void onFluxPostInit() {
        Map<Block, String> reverseNameLookup = new HashMap<>();
        for(var entry : Block.blocksByName.entrySet()) {
            reverseNameLookup.put(entry.getValue(), entry.getKey());
        }

        Map<Identifier, String> blockNames = new HashMap<>();
        for(var entry : Block.blocksByStringId.entrySet()) {
            blockNames.put(Identifier.fromString(entry.getKey()), reverseNameLookup.get(entry.getValue()));
        }

        AccessableRegistry<IModBlock> blocks = FluxRegistries.BLOCKS.access();
        for(Identifier blockId : blocks.getRegisteredNames()) {
            Item item = new BlockItem(blockId, blockNames.get(blockId));
            ItemRegistry.allItems.register(item.itemId, item);
        }

        AccessableRegistry<Item> items = ItemRegistry.allItems.access();
        for(Identifier itemId : items.getRegisteredNames()) {
            Item item = items.get(itemId);
            ItemRegistry.ITEM_FINALIZERS.register(itemId, item.view::initialize);
        }

    }

}
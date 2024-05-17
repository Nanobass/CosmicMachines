package net.paxyinc;

import dev.crmodders.flux.api.block.IModBlock;
import dev.crmodders.flux.engine.GameLoader;
import dev.crmodders.flux.registry.FluxRegistries;
import dev.crmodders.flux.registry.registries.AccessableRegistry;
import dev.crmodders.flux.tags.Identifier;
import finalforeach.cosmicreach.blocks.Block;
import finalforeach.cosmicreach.gamestates.GameState;
import net.fabricmc.api.ModInitializer;
import net.paxyinc.item.Item;
import net.paxyinc.item.items.BlockItem;
import net.paxyinc.item.loading.RegisterItems;
import net.paxyinc.machines.content.blocks.AutoSmelterBlock;
import net.paxyinc.machines.content.blocks.CableBlock;

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
    }

    public void onFluxInit() {
        FluxRegistries.BLOCK_FACTORIES.add(CableBlock::new);
        FluxRegistries.BLOCK_FACTORIES.add(AutoSmelterBlock::new);
    }

    public void onFluxPostInit() {
        AccessableRegistry<IModBlock> blocks = FluxRegistries.BLOCKS.access();

        Map<Block, String> reverseNameLookup = new HashMap<>();
        for(var entry : Block.blocksByName.entrySet()) {
            reverseNameLookup.put(entry.getValue(), entry.getKey());
        }

        Map<Identifier, String> blockNames = new HashMap<>();
        for(var entry : Block.blocksByStringId.entrySet()) {
            blockNames.put(Identifier.fromString(entry.getKey()), reverseNameLookup.get(entry.getValue()));
        }

        for(Identifier blockId : blocks.getRegisteredNames()) {
            Item item = new BlockItem(blockId, blockNames.get(blockId));
            MachineRegistries.ITEMS.register(item.itemId, item);
        }

        AccessableRegistry<Item> items = MachineRegistries.ITEMS.access();
        for(Identifier itemId : items.getRegisteredNames()) {
            Item item = items.get(itemId);
            MachineRegistries.ITEM_FINALIZERS.register(itemId, item.view::initialize);
        }

        GameLoader loader = (GameLoader) GameState.currentGameState;
        loader.addStage(new RegisterItems());

    }

}

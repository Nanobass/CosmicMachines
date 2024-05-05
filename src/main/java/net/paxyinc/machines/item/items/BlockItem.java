package net.paxyinc.machines.item.items;

import dev.crmodders.flux.tags.Identifier;
import finalforeach.cosmicreach.blocks.Block;
import net.paxyinc.machines.item.Item;
import net.paxyinc.machines.item.views.BlockItemView;

public class BlockItem extends Item {

    public final Block block;

    public BlockItem(Identifier blockId, String itemName) {
        itemId = blockId;
        name = itemName;
        block = Block.blocksByStringId.get(blockId.toString());
        view = new BlockItemView(block.getDefaultBlockState());
    }
}

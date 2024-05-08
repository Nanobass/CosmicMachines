package net.paxyinc.machines.commands;

import com.insertsoda.craterchat.api.v1.Command;
import com.insertsoda.craterchat.api.v1.CommandSource;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import dev.crmodders.flux.tags.Identifier;
import net.paxyinc.machines.item.Item;
import net.paxyinc.machines.item.ItemRegistry;
import net.paxyinc.machines.item.inventories.PlayerInventory;

public class TestCommand implements Command {

    private static int giveItem(CommandSource source, String itemId, int amount) {
        Item item = ItemRegistry.allItems.access().get(Identifier.fromString(itemId));
        if(item == null) return 0;
        PlayerInventory.inventory.pickupItems(item, amount);
        return 1;
    }

    @Override
    public void register(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes( source -> {
            giveItem(source.getSource(), "cosmicmachines:cable", 64);
            giveItem(source.getSource(), "cosmicmachines:smelter", 64);
            giveItem(source.getSource(), "base:asphalt", 64);
            return 1;
        });
    }
    @Override
    public String getName() {
        return "test";
    }
    @Override
    public String getDescription() {
        return "Gives Items to the Player";
    }
}

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

public class GiveCommand implements Command {

    private static int giveItem(CommandSource source, String itemId, int amount) {
        Item item = ItemRegistry.allItems.access().get(Identifier.fromString(itemId));
        if(item == null) return 0;
        PlayerInventory.inventory.put(item, amount);
        return 1;
    }

    @Override
    public void register(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(
            RequiredArgumentBuilder.<CommandSource, String>argument("item", StringArgumentType.word())
                .then(
                    RequiredArgumentBuilder.<CommandSource, Integer>argument("amount", IntegerArgumentType.integer(1))
                        .executes(context -> giveItem(context.getSource(), StringArgumentType.getString(context, "item"), IntegerArgumentType.getInteger(context, "amount")))
                    )
                    .executes(context -> giveItem(context.getSource(), StringArgumentType.getString(context, "item"), 1))
        );
    }
    @Override
    public String getName() {
        return "give";
    }
    @Override
    public String getDescription() {
        return "Gives Items to the Player";
    }
}

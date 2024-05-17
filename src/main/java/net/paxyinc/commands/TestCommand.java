package net.paxyinc.commands;

import com.insertsoda.craterchat.api.v1.Command;
import com.insertsoda.craterchat.api.v1.CommandSource;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.crmodders.flux.tags.Identifier;
import finalforeach.cosmicreach.gamestates.InGame;
import net.paxyinc.MachineRegistries;
import net.paxyinc.entities.BetterEntity;
import net.paxyinc.interfaces.PlayerInterface;
import net.paxyinc.item.Item;
import net.paxyinc.item.inventories.PlayerInventory;

public class TestCommand implements Command {

    private static int giveItem(CommandSource source, String itemId, int amount) {
        Item item = MachineRegistries.ITEMS.access().get(Identifier.fromString(itemId));
        if(item == null) return 0;

        PlayerInterface pi = (PlayerInterface) InGame.getLocalPlayer();
        PlayerInventory playerInventory = pi.getInventory();

        playerInventory.put(item, amount);
        return 1;
    }

    @Override
    public void register(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes( source -> {
            giveItem(source.getSource(), "cosmicmachines:cable", 64);
            giveItem(source.getSource(), "cosmicmachines:smelter", 64);
            giveItem(source.getSource(), "base:asphalt", 64);
            ((BetterEntity) InGame.getLocalPlayer().getEntity()).hurt(25);
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

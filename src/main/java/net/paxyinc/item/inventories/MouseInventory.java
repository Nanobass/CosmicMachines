package net.paxyinc.item.inventories;

import finalforeach.cosmicreach.entities.Entity;
import finalforeach.cosmicreach.entities.Player;
import finalforeach.cosmicreach.gamestates.InGame;
import net.paxyinc.entities.ItemEntity;
import net.paxyinc.item.ItemInventory;
import net.paxyinc.item.ItemSlot;

public class MouseInventory extends ItemInventory {

    public static MouseInventory mouseInventory = new MouseInventory();

    public final ItemSlot slot;

    public MouseInventory() {
        super(1);
        slot = slots.get(0);
    }

    public void dropItems(Player at) {
        Entity entity = at.getEntity();
        for(ItemEntity item : ItemEntity.dropItem(at.getZone(InGame.world), entity, 0, 1.75F, 0, this)) {
            item.velocity.set(entity.viewDirection).scl(10);
        }
        clear();
    }

}

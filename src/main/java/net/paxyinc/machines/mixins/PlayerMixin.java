package net.paxyinc.machines.mixins;

import com.badlogic.gdx.math.MathUtils;
import finalforeach.cosmicreach.entities.Entity;
import finalforeach.cosmicreach.entities.Player;
import finalforeach.cosmicreach.settings.Controls;
import finalforeach.cosmicreach.world.Zone;
import net.paxyinc.machines.entities.ItemEntity;
import net.paxyinc.machines.entities.RenderableEntity;
import net.paxyinc.machines.item.Item;
import net.paxyinc.machines.item.ItemSlot;
import net.paxyinc.machines.item.ItemStack;
import net.paxyinc.machines.item.inventories.PlayerInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerMixin {

    @Shadow private Entity controlledEntity;

    @Inject(method = "update", at = @At("TAIL"))
    private void update(Zone zone, CallbackInfo ci) {
        if(Controls.dropItemPressed()) {
            ItemSlot selected = PlayerInventory.inventory.getSelectedItem();
            Item item = selected.itemStack == null ? null : selected.itemStack.item;
            if(selected.take(1)) {
                ItemEntity entity = new ItemEntity(item, 1);
                RenderableEntity.spawn(zone, controlledEntity.position.x, controlledEntity.position.y + 1.75F, controlledEntity.position.z, entity);
                entity.velocity.set(controlledEntity.viewDirection).scl(10);
            }
        }
    }

}

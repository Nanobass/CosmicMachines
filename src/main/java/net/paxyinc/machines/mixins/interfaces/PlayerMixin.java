package net.paxyinc.machines.mixins.interfaces;

import com.badlogic.gdx.math.Vector3;
import finalforeach.cosmicreach.entities.Entity;
import finalforeach.cosmicreach.entities.Player;
import finalforeach.cosmicreach.settings.Controls;
import finalforeach.cosmicreach.world.Zone;
import net.paxyinc.machines.entities.BetterEntity;
import net.paxyinc.machines.entities.ItemEntity;
import net.paxyinc.machines.item.Item;
import net.paxyinc.machines.item.ItemSlot;
import net.paxyinc.machines.item.inventories.PlayerInventory;
import net.paxyinc.machines.nbt.NbtSerializable;
import net.paxyinc.machines.nbt.NbtSerializer;
import net.querz.nbt.tag.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerMixin implements NbtSerializable {

    @Shadow public String zoneId;
    @Shadow private Entity controlledEntity;
    @Shadow public boolean isSprinting;
    @Shadow public boolean isProne;
    @Shadow public boolean isProneIntended;
    @Shadow public boolean isSneakIntended;
    @Shadow float lastPlayerSpeed;
    @Shadow Vector3 standingViewPositionOffset;
    @Shadow Vector3 proneViewPositionOffset;
    @Shadow Vector3 sneakingViewPositionOffset;
    @Shadow private Vector3 lastVelocity;
    @Shadow private Vector3 wallJumpVelocity;

    @Inject(method = "update", at = @At("TAIL"))
    private void update(Zone zone, CallbackInfo ci) {
        if(Controls.dropItemPressed()) {
            ItemSlot selected = PlayerInventory.inventory.getSelectedItem();
            Item item = selected.itemStack == null ? null : selected.itemStack.item;
            if(selected.take(1)) {
                ItemEntity.dropItem(zone, controlledEntity, 0, 1.75F, 0, item, 1).velocity.set(controlledEntity.viewDirection).scl(10);
            }
        }
    }

    @Inject(method = "setEntity", at = @At("HEAD"))
    private void setEntity(Entity entity, CallbackInfo ci) {
        if(!(entity instanceof BetterEntity)) throw new RuntimeException("Entity is not BetterEntity");
        System.err.println(entity);
    }

    @Override
    public void write(CompoundTag nbt) {
        nbt.putString("zoneId", zoneId);
        nbt.put("controlledEntity", NbtSerializer.write((NbtSerializable) controlledEntity));
        CompoundTag inventory = new CompoundTag();
        PlayerInventory.inventory.write(inventory);
        nbt.put("inventory", inventory);
    }

    @Override
    public void read(CompoundTag nbt) {
        zoneId = nbt.getString("zoneId");
        controlledEntity = NbtSerializer.read(nbt.getCompoundTag("controlledEntity"));
        CompoundTag inventory = nbt.getCompoundTag("inventory");
        PlayerInventory.inventory.clear();
        PlayerInventory.inventory.read(inventory);
    }

}

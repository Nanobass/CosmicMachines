package net.paxyinc.mixins.interfaces;

import com.badlogic.gdx.math.Vector3;
import finalforeach.cosmicreach.entities.Entity;
import finalforeach.cosmicreach.entities.Player;
import finalforeach.cosmicreach.gamestates.PauseMenu;
import finalforeach.cosmicreach.settings.Controls;
import finalforeach.cosmicreach.world.Zone;
import net.paxyinc.entities.BetterEntity;
import net.paxyinc.entities.ItemEntity;
import net.paxyinc.interfaces.PlayerInterface;
import net.paxyinc.item.Item;
import net.paxyinc.item.ItemSlot;
import net.paxyinc.item.inventories.PlayerInventory;
import net.paxyinc.nbt.NbtSerializable;
import net.paxyinc.nbt.NbtSerializer;
import net.querz.nbt.tag.CompoundTag;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin implements NbtSerializable, PlayerInterface {

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

    private final PlayerInventory inventory = new PlayerInventory();

    @Shadow protected abstract void respawn(Zone zone);

    @Inject(method = "update", at = @At("TAIL"))
    private void update(Zone zone, CallbackInfo ci) {

        BetterEntity entity = (BetterEntity) controlledEntity;
        if(entity.dead()) {
            ItemEntity.dropItem(zone, controlledEntity, 0, 0, 0, inventory);
            inventory.clear();
            respawn(zone);
            entity.health = 100;
        }

        if(Controls.dropItemPressed()) {
            ItemSlot selected = inventory.getSelectedItem();
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
        CompoundTag inventoryTag = new CompoundTag();
        inventory.write(inventoryTag);
        nbt.put("inventory", inventoryTag);
    }

    @Override
    public void read(CompoundTag nbt) {
        zoneId = nbt.getString("zoneId");
        controlledEntity = NbtSerializer.read(nbt.getCompoundTag("controlledEntity"));
        CompoundTag inventoryTag = nbt.getCompoundTag("inventory");
        inventory.read(inventoryTag);
    }


    @Override
    public PlayerInventory getInventory() {
        return inventory;
    }

}

package net.paxyinc.machines.mixins;

import com.badlogic.gdx.math.Vector3;
import finalforeach.cosmicreach.entities.Entity;
import finalforeach.cosmicreach.entities.Player;
import finalforeach.cosmicreach.settings.Controls;
import finalforeach.cosmicreach.world.World;
import finalforeach.cosmicreach.world.Zone;
import net.paxyinc.machines.entities.BetterEntity;
import net.paxyinc.machines.entities.ItemEntity;
import net.paxyinc.machines.item.Item;
import net.paxyinc.machines.item.ItemSlot;
import net.paxyinc.machines.item.inventories.PlayerInventory;
import net.paxyinc.machines.nbt.NbtSerializable;
import net.paxyinc.machines.interfaces.PlayerInterface;
import net.querz.nbt.tag.CompoundTag;
import org.checkerframework.checker.units.qual.C;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(Player.class)
public class PlayerMixin implements NbtSerializable, PlayerInterface {

    @Shadow public String zoneId;
    @Shadow private Entity controlledEntity;
    private UUID controlledEntityUUID = UUID.randomUUID();
    @Shadow public boolean isSprinting;
    @Shadow public boolean isProne;
    @Shadow public boolean isProneIntended;
    @Shadow public boolean isSneakIntended;
    @Shadow float lastPlayerSpeed = 5.0F;
    @Shadow Vector3 standingViewPositionOffset = new Vector3(0.0F, 1.8F, 0.0F);
    @Shadow Vector3 proneViewPositionOffset = new Vector3(0.0F, 0.9F, 0.0F);
    @Shadow Vector3 sneakingViewPositionOffset = new Vector3(0.0F, 1.37F, 0.0F);
    @Shadow private Vector3 lastVelocity = new Vector3();
    @Shadow private Vector3 wallJumpVelocity = new Vector3();

    private BetterEntity controlledEntity() {
        return (BetterEntity) (Object) controlledEntity;
    }


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
        if(!BetterEntity.class.isInstance(entity)) throw new RuntimeException("Entity is not BetterEntity");
        System.err.println(entity);
    }

    @Override
    public void write(CompoundTag nbt) {
        nbt.putString("zoneId", zoneId);
        nbt.putString("controlledEntity", controlledEntityUUID.toString());
        CompoundTag inventory = new CompoundTag();
        PlayerInventory.inventory.write(inventory);
        nbt.put("inventory", inventory);
    }

    @Override
    public void read(CompoundTag nbt) {
        zoneId = nbt.getString("zoneId");
        controlledEntityUUID = UUID.fromString(nbt.getString("controlledEntity"));
        CompoundTag inventory = nbt.getCompoundTag("inventory");
        PlayerInventory.inventory.clear();
        PlayerInventory.inventory.read(inventory);
    }

    @Override
    public UUID getControlledEntityUUID() {
        return controlledEntityUUID;
    }

    @Override
    public void initialize(World world, BetterEntity controlledEntity) {
        this.controlledEntity = controlledEntity;
        this.controlledEntityUUID = controlledEntity.uuid();
    }

}

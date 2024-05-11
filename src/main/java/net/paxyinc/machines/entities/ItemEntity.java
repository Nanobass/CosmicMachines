package net.paxyinc.machines.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.entities.Entity;
import finalforeach.cosmicreach.world.Zone;
import net.paxyinc.machines.item.AbstractItemList;
import net.paxyinc.machines.item.IItemView;
import net.paxyinc.machines.item.Item;
import net.paxyinc.machines.item.ItemStack;
import net.paxyinc.machines.item.lists.ItemList;
import net.querz.nbt.tag.CompoundTag;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class ItemEntity extends BetterEntity {

    public static ItemEntity dropItem(Zone zone, BlockPosition position, Item item, int amount) {
        ItemEntity entity = new ItemEntity(item, amount);
        BetterEntity.spawn(zone, position.getGlobalX() + 0.5f, position.getGlobalY() + 0.5f, position.getGlobalZ() + 0.5f, entity);
        return entity;
    }

    public static ItemEntity dropItem(Zone zone, Entity from, float x, float y, float z, Item item, int amount) {
        ItemEntity entity = new ItemEntity(item, amount);
        BetterEntity.spawn(zone, from.position.x + x, from.position.y + y, from.position.z + z, entity);
        return entity;
    }

    public static List<ItemEntity> dropItems(Zone zone, BlockPosition position, AbstractItemList items) {
        return dropItems(zone, position.getGlobalX(), position.getGlobalY(), position.getGlobalZ(), items);
    }

    public static List<ItemEntity> dropItems(Zone zone, float x, float y, float z, AbstractItemList items) {
        List<ItemEntity> entities = new ArrayList<>();
        for(Item item : items) {
            int amount = items.amount(item);
            ItemEntity entity = new ItemEntity(item, amount);
            BetterEntity.spawn(zone, x, y, z, entity);
            entities.add(entity);
        }
        return entities;
    }

    public ItemStack items;

    public float timer = 0.5f;

    public ItemEntity() {
        localBoundingBox.min.set(-0.15F, -0.15F, -0.15F);
        localBoundingBox.max.set(0.15F, 0.15F, 0.15F);
        localBoundingBox.update();
    }

    public ItemEntity(Item item, int amount) {
        this();
        items = new ItemStack(item, amount, Integer.MAX_VALUE);
    }

    @Override
    public void render(Camera camera) {
        Matrix4 projViewTrans = new Matrix4(camera.combined);
        projViewTrans.translate(position);
        projViewTrans.translate(localBoundingBox.min);
        projViewTrans.scale(localBoundingBox.getWidth(), localBoundingBox.getHeight(), localBoundingBox.getDepth());
        items.item.view.render(camera, projViewTrans);
        timer -= Gdx.graphics.getDeltaTime();
    }

    @Override
    public void write(CompoundTag nbt) {
        super.write(nbt);
        items.write(nbt);
    }

    @Override
    public void read(CompoundTag nbt) {
        super.read(nbt);
        items = new ItemStack();
        items.read(nbt);
    }
}

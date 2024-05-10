package net.paxyinc.machines.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Matrix4;
import net.paxyinc.machines.item.IItemView;
import net.paxyinc.machines.item.Item;
import net.paxyinc.machines.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class ItemEntity extends RenderableEntity {

    public ItemStack items;
    protected IItemView renderer;

    public float timer = 0.5f;

    public ItemEntity(Item item, int amount) {
        localBoundingBox.min.set(-0.15F, -0.15F, -0.15F);
        localBoundingBox.max.set(0.15F, 0.15F, 0.15F);
        localBoundingBox.update();
        items = new ItemStack(item, amount, Integer.MAX_VALUE);
        renderer = item.view;
    }

    @Override
    public void render(Camera camera) {
        Matrix4 projViewTrans = new Matrix4(camera.combined);
        projViewTrans.translate(position);
        projViewTrans.translate(localBoundingBox.min);
        projViewTrans.scale(localBoundingBox.getWidth(), localBoundingBox.getHeight(), localBoundingBox.getDepth());
        renderer.render(camera, projViewTrans);
        timer -= Gdx.graphics.getDeltaTime();
    }
}

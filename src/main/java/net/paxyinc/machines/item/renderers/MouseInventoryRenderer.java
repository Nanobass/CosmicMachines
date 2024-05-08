package net.paxyinc.machines.item.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.crmodders.flux.ui.UIRenderer;
import dev.crmodders.flux.ui.text.TextBatch;
import net.paxyinc.machines.item.*;
import net.paxyinc.machines.item.inventories.MouseInventory;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class MouseInventoryRenderer implements IItemInventoryRenderer {

    protected PerspectiveCamera itemCamera;
    protected FitViewport itemViewport;

    private ItemInventory inventory;
    private ItemSlotPosition slot;

    public MouseInventoryRenderer(ItemInventory inventory) {
        itemCamera = new PerspectiveCamera(1.1F, 100.0F, 100.0F);
        itemViewport = new FitViewport(100.0F, 100.0F, itemCamera);
        itemCamera.near = 30.0F;
        itemCamera.far = 100.0F;
        itemCamera.position.set(50.0F, 50.0F, 50.0F);
        itemCamera.lookAt(0.5F, 0.5F, 0.5F);
        itemCamera.update();
        this.inventory = inventory;
        slot = new ItemSlotPosition(inventory.slots.get(0));
        slot.rectangle.width = 32;
        slot.rectangle.height = 32;
    }

    @Override
    public void updateUI(Viewport uiViewport, Vector2 mouse) {
        slot.rectangle.x = mouse.x;
        slot.rectangle.y = mouse.y;
    }

    @Override
    public void render(Viewport uiViewport, Camera uiCamera) {
        float ratioX = (float)uiViewport.getScreenWidth() / uiViewport.getWorldWidth();
        float ratioY = (float)uiViewport.getScreenHeight() / uiViewport.getWorldHeight();
        Vector2 tmp = new Vector2();
        if(slot.slot.itemStack == null) return;
        IItemView view = slot.slot.itemStack.item.view;
        float sx = slot.rectangle.x - slot.rectangle.width / 2;
        float sy = slot.rectangle.y + slot.rectangle.height / 2;
        float sw = slot.rectangle.width * ratioX;
        float sh = slot.rectangle.height * ratioY;
        tmp.set(sx, sy);
        uiViewport.project(tmp);
        itemViewport.setScreenBounds((int) tmp.x, (int) tmp.y, (int)sw, (int)sh);
        itemViewport.apply();

        Gdx.gl.glEnable(GL11.GL_CULL_FACE);
        Gdx.gl.glDepthFunc(GL11.GL_ALWAYS);
        Gdx.gl.glCullFace(GL11.GL_BACK);
        Gdx.gl.glEnable(GL11.GL_BLEND);
        Gdx.gl.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        view.render(itemCamera);
        Gdx.gl.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glEnable(GL11.GL_DEPTH_TEST);
        Gdx.gl.glDepthFunc(GL11.GL_LESS);
        Gdx.gl.glEnable(GL11.GL_CULL_FACE);
        Gdx.gl.glCullFace(GL11.GL_BACK);
        Gdx.gl.glEnable(GL11.GL_BLEND);
        Gdx.gl.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glActiveTexture(GL13.GL_TEXTURE0);
        Gdx.gl.glBindTexture(GL11.GL_TEXTURE_2D, 0);

        if(slot.slot.itemStack.amount == 1) return;
        TextBatch batch = UIRenderer.uiRenderer.createText(UIRenderer.cosmicReachFont, 12f, String.valueOf(slot.slot.itemStack.amount), Color.WHITE);
        UIRenderer.uiRenderer.drawBatch(batch, slot.rectangle.x - slot.rectangle.width / 2, slot.rectangle.y - slot.rectangle.height / 2 + 18.0F);

    }

    @Override
    public ItemSlotPosition atMouse(Vector2 mouse) {
        return slot;
    }
}

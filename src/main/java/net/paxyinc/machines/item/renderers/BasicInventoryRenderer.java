package net.paxyinc.machines.item.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.crmodders.flux.api.gui.base.BaseElement;
import dev.crmodders.flux.ui.UIRenderer;
import dev.crmodders.flux.ui.text.TextBatch;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.items.ItemCatalog;
import finalforeach.cosmicreach.lang.Lang;
import finalforeach.cosmicreach.ui.FontRenderer;
import net.paxyinc.machines.item.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.util.*;
import java.util.function.Consumer;

public class BasicInventoryRenderer implements IItemInventoryRenderer {

    protected PerspectiveCamera itemCamera;
    protected FitViewport itemViewport;

    protected SpriteBatch batch;
    protected ShapeDrawer shape;

    protected Color itemBackgroundColor = new Color(0.54f, 0.54f, 0.54f, 1.0f);
    protected Color itemHighlightedColor = new Color(0.64f, 0.64f, 0.64f, 1.0f);

    public float x, y, width, height;

    protected final ItemInventory inventory;
    protected final List<ItemSlotPosition> positions;

    public BasicInventoryRenderer(ItemInventory inventory) {
        this.inventory = inventory;
        batch = new SpriteBatch();
        shape = new ShapeDrawer(batch, new TextureRegion(UIRenderer.white));
        positions = new ArrayList<>();
        itemCamera = new PerspectiveCamera(1.1F, 100.0F, 100.0F);
        itemViewport = new FitViewport(100.0F, 100.0F, itemCamera);
        itemCamera.near = 30.0F;
        itemCamera.far = 100.0F;
        itemCamera.position.set(50.0F, 50.0F, 50.0F);
        itemCamera.lookAt(0.5F, 0.5F, 0.5F);
        itemCamera.update();
    }

    public List<ItemSlotPosition> createArea(int startSlot, int endSlot) {
        List<ItemSlotPosition> positions = new ArrayList<>();
        for(int slot = startSlot; slot < endSlot; slot++) {
            Rectangle rect = new Rectangle();
            rect.width = 32;
            rect.height = 32;
            positions.add(new ItemSlotPosition(inventory.slots.get(slot)));
        }
        this.positions.addAll(positions);
        return positions;
    }

    public void setState(List<ItemSlotPosition> slots, ItemSlotPositionState state) {
        slots.forEach(slot -> slot.state = state);
    }

    public void orState(ItemSlot slot, ItemSlotPositionState state) {
        for(ItemSlotPosition pos : positions) {
            if(pos.slot.slotId == slot.slotId && pos.state != ItemSlotPositionState.DISABLED) pos.state = state;
        }
    }

    public void layoutAreaAroundCenterPoint(List<ItemSlotPosition> slots, int wrapWidth, float centerX, float centerY, float padding, float size) {
        float lineWidth = padding + wrapWidth * (size + padding);
        float lineHeight = padding + (float)Math.ceil((float)slots.size() / (float)wrapWidth) * (size + padding);
        for(int index = 0; index < slots.size(); index++) {
            ItemSlotPosition position = slots.get(index);
            Rectangle rect = position.rectangle;
            int x = index % wrapWidth;
            int y = index / wrapWidth;
            rect.x = padding + x * (size + padding) + centerX - lineWidth / 2.0F;
            rect.y = padding + y * (size + padding) + centerY - lineHeight / 2.0F;
            rect.width = size;
            rect.height = size;
        }
    }

    public List<ItemSlotPosition> getPositions(ItemSlot slot) {
        List<ItemSlotPosition> res = new ArrayList<>();
        for(ItemSlotPosition pos : positions) {
            if(pos.slot.slotId == slot.slotId) res.add(pos);
        }
        return res;
    }

    @Override
    public void updateUI(Viewport uiViewport, Vector2 mouse) {

    }

    @Override
    public void render(Viewport uiViewport, Camera uiCamera) {
        uiViewport.apply();

        Gdx.gl.glDisable(GL11.GL_CULL_FACE);

        List<ItemSlotPosition> positions = new ArrayList<>();
        for(ItemSlotPosition position : this.positions) {
            if(position.state != ItemSlotPositionState.DISABLED) positions.add(position);
        }

        batch.setProjectionMatrix(uiCamera.combined);
        batch.begin();
        for(ItemSlotPosition position : positions) {
            Consumer<Color> drawFunc = color -> shape.filledRectangle(this.x + position.rectangle.x, this.y + position.rectangle.y, position.rectangle.width, position.rectangle.height, color);
            Consumer<Color> borderFunc = color -> shape.rectangle(this.x + position.rectangle.x, this.y + position.rectangle.y, position.rectangle.width, position.rectangle.height, color, 2);

            switch (position.state) {
                case HOVERED:
                    drawFunc.accept(itemHighlightedColor);
                    break;
                case SELECTED:
                    borderFunc.accept(itemHighlightedColor);
                case VISIBLE:
                    drawFunc.accept(itemBackgroundColor);
                    break;
                case DISABLED:
                default:
                    break;
            }
        }
        batch.end();

        Gdx.gl.glEnable(GL11.GL_CULL_FACE);
        Gdx.gl.glDepthFunc(GL11.GL_ALWAYS);
        Gdx.gl.glCullFace(GL11.GL_BACK);
        Gdx.gl.glEnable(GL11.GL_BLEND);
        Gdx.gl.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        float ratioX = (float)uiViewport.getScreenWidth() / uiViewport.getWorldWidth();
        float ratioY = (float)uiViewport.getScreenHeight() / uiViewport.getWorldHeight();
        Vector2 tmp = new Vector2();
        for(ItemSlotPosition position : positions) {
            Rectangle rect = position.rectangle;
            ItemSlot slot = position.slot;
            if(slot.itemStack != null) {
                IItemView view = slot.itemStack.item.view;
                float sx = this.x + rect.x;
                float sy = this.y + rect.y + rect.height;
                float sw = rect.width * ratioX;
                float sh = rect.height * ratioY;
                tmp.set(sx, sy);
                uiViewport.project(tmp);
                itemViewport.setScreenBounds((int) tmp.x, (int) tmp.y, (int)sw, (int)sh);
                itemViewport.apply();
                view.render(itemCamera);
            }
        }

        Gdx.gl.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glEnable(GL11.GL_DEPTH_TEST);
        Gdx.gl.glDepthFunc(GL11.GL_LESS);
        Gdx.gl.glEnable(GL11.GL_CULL_FACE);
        Gdx.gl.glCullFace(GL11.GL_BACK);
        Gdx.gl.glEnable(GL11.GL_BLEND);
        Gdx.gl.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glActiveTexture(GL13.GL_TEXTURE0);
        Gdx.gl.glBindTexture(GL11.GL_TEXTURE_2D, 0);

        for(ItemSlotPosition position : positions) {
            if(position.slot.itemStack == null || position.slot.itemStack.amount == 1) continue;
            TextBatch batch = UIRenderer.uiRenderer.createText(UIRenderer.cosmicReachFont, 12f, String.valueOf(position.slot.itemStack.amount), Color.WHITE);
            UIRenderer.uiRenderer.drawBatch(batch, this.x + position.rectangle.x, this.y + position.rectangle.y + 18.0F);
        }

    }

    public ItemSlotPosition atMouse(Vector2 mouse) {
        for(ItemSlotPosition pos : positions) {
            if(pos.rectangle.contains(mouse) && pos.state != ItemSlotPositionState.DISABLED) return pos;
        }
        return null;
    }

}

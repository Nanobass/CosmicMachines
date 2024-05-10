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
import dev.crmodders.flux.ui.Component;
import dev.crmodders.flux.ui.UIRenderer;
import dev.crmodders.flux.ui.text.TextBatch;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.items.ItemCatalog;
import finalforeach.cosmicreach.lang.Lang;
import finalforeach.cosmicreach.ui.FontRenderer;
import net.paxyinc.machines.item.*;
import net.paxyinc.machines.ui.BaseItemElement;
import net.paxyinc.machines.ui.InGameUI;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;

import static dev.crmodders.flux.ui.UIRenderer.uiRenderer;

public class BasicInventoryRenderer extends InGameUI implements IItemInventoryRenderer {

    protected final ItemInventory inventory;
    protected final List<BaseItemElement> positions;

    public BasicInventoryRenderer(ItemInventory inventory) {
        this.inventory = inventory;
        positions = new ArrayList<>();
    }

    public List<BaseItemElement> createArea(int startSlot, int endSlot) {
        List<BaseItemElement> positions = new ArrayList<>();
        for(int slot = startSlot; slot < endSlot; slot++) {
            BaseItemElement element = new BaseItemElement(inventory.slots.get(slot));
            element.width = 32;
            element.height = 32;
            positions.add(element);
        }
        this.positions.addAll(positions);
        this.uiElements.addAll(positions);
        return positions;
    }

    public void setState(List<BaseItemElement> slots, ItemSlotPositionState state) {
        slots.forEach(slot -> slot.state = state);
    }

    public void orState(ItemSlot slot, ItemSlotPositionState state) {
        for(BaseItemElement pos : positions) {
            if(pos.slot.slotId == slot.slotId && pos.state != ItemSlotPositionState.DISABLED) pos.state = state;
        }
    }

    public void layoutAreaAroundCenterPoint(List<BaseItemElement> slots, int wrapWidth, float centerX, float centerY, float padding, float size) {
        float lineWidth = padding + wrapWidth * (size + padding);
        float lineHeight = padding + (float)Math.ceil((float)slots.size() / (float)wrapWidth) * (size + padding);
        for(int index = 0; index < slots.size(); index++) {
            BaseItemElement position = slots.get(index);
            int x = index % wrapWidth;
            int y = index / wrapWidth;
            position.x = padding + x * (size + padding) + centerX - lineWidth / 2.0F;
            position.y = padding + y * (size + padding) + centerY - lineHeight / 2.0F;
            position.width = size;
            position.height = size;
        }
    }

    public List<BaseItemElement> positions(ItemSlot slot) {
        List<BaseItemElement> res = new ArrayList<>();
        for(BaseItemElement pos : positions) {
            if(pos.slot.slotId == slot.slotId) res.add(pos);
        }
        return res;
    }

    @Override
    public InGameUI getUI() {
        return this;
    }

    @Override
    public void render(Viewport uiViewport, Camera uiCamera, Vector2 mouse) {
        positions.forEach(BaseItemElement::repaint);
        super.render(uiViewport, uiCamera, mouse);
    }

    public BaseItemElement atMouse(Vector2 mouse) {
        for(BaseItemElement pos : positions) {
            if(pos.contains(mouse) && pos.state != ItemSlotPositionState.DISABLED) return pos;
        }
        return null;
    }

}

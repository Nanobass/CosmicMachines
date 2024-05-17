package net.paxyinc.item.renderers;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.crmodders.flux.ui.Component;
import finalforeach.cosmicreach.ui.HorizontalAnchor;
import finalforeach.cosmicreach.ui.VerticalAnchor;
import net.paxyinc.item.IItemInventoryRenderer;
import net.paxyinc.item.ItemInventory;
import net.paxyinc.item.ItemSlot;
import net.paxyinc.ui.elements.BaseItemElement;

import java.util.ArrayList;
import java.util.List;

public class BasicInventoryRenderer<T extends ItemInventory> implements IItemInventoryRenderer {

    protected final T inventory;

    protected final List<BaseItemElement> positions;

    public BasicInventoryRenderer(T inventory) {
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
        return positions;
    }

    public void layoutAreaAroundCenterPoint(List<BaseItemElement> slots, int wrapWidth, float centerX, float centerY, float padding, float size) {
        float lineWidth = padding + (float) wrapWidth * (size + padding);
        float lineHeight = padding + (float)Math.ceil((float)slots.size() / (float)wrapWidth) * (size + padding);
        for(int index = 0; index < slots.size(); index++) {
            BaseItemElement position = slots.get(index);
            float x = index % wrapWidth;
            float y = index / wrapWidth;
            position.x = padding + x * (size + padding) + centerX - lineWidth / 2.0F + size / 2;
            position.y = padding + y * (size + padding) + centerY - lineHeight / 2.0F + size / 2;
            position.width = size;
            position.height = size;
        }
    }

    public void setAlignment(List<BaseItemElement> positions, HorizontalAnchor hAnchor, VerticalAnchor vAnchor) {
        for(BaseItemElement element : positions) {
            element.setAnchors(hAnchor, vAnchor);
        }
    }

    public void setMouseHover(List<BaseItemElement> positions, boolean enabled) {
        for(BaseItemElement element : positions) {
            element.shouldRenderHovered = enabled;
        }
    }

    public List<BaseItemElement> positions(ItemSlot slot) {
        List<BaseItemElement> res = new ArrayList<>();
        for(BaseItemElement pos : positions) {
            if(pos.slot.slotId == slot.slotId) res.add(pos);
        }
        return res;
    }

    public void resetFlags(List<BaseItemElement> positions) {
        for(BaseItemElement pos : positions) {
            pos.visible = false;
            pos.isSelected = false;
        }
    }

    public void setVisible(List<BaseItemElement> positions, boolean visible) {
        positions.forEach(i -> i.visible = visible);
    }

    @Override
    public List<Component> render(Viewport uiViewport, Camera uiCamera, Vector2 mouse) {
        return new ArrayList<>(positions);
    }

    public BaseItemElement atMouse(Viewport viewport, Vector2 mouse) {
        for(BaseItemElement pos : positions) {
            if(pos.isHoveredOver(viewport, mouse.x, mouse.y) && pos.visible) return pos;
        }
        return null;
    }

    @Override
    public ItemInventory getInventory() {
        return inventory;
    }
}

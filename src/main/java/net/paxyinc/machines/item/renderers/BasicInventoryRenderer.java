package net.paxyinc.machines.item.renderers;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.crmodders.flux.ui.Component;
import net.paxyinc.machines.item.IItemInventoryRenderer;
import net.paxyinc.machines.item.ItemInventory;
import net.paxyinc.machines.item.ItemSlot;
import net.paxyinc.machines.ui.BaseItemElement;

import java.util.ArrayList;
import java.util.List;

public class BasicInventoryRenderer implements IItemInventoryRenderer {

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
        List<Component> components = new ArrayList<>();
        positions.forEach(components::add);
        return components;
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

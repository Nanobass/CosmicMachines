package net.paxyinc.item.renderers;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.crmodders.flux.ui.Component;
import net.paxyinc.item.IItemInventoryRenderer;
import net.paxyinc.item.ItemInventory;
import net.paxyinc.ui.elements.BaseItemElement;

import java.util.List;

public class MouseInventoryRenderer implements IItemInventoryRenderer {

    private BaseItemElement slot;

    public MouseInventoryRenderer(ItemInventory inventory) {
        slot = new BaseItemElement(inventory.slots.get(0));
        slot.shouldRenderBackground = false;
        slot.shouldRenderName = false;
        slot.width = 32;
        slot.height = 32;
    }

    @Override
    public List<Component> render(Viewport uiViewport, Camera uiCamera, Vector2 mouse) {
        slot.x = mouse.x;
        slot.y = mouse.y;
        slot.repaint();
        return List.of(slot);
    }

    @Override
    public BaseItemElement atMouse(Viewport viewport, Vector2 mouse) {
        return slot;
    }

    @Override
    public ItemInventory getInventory() {
        return slot.slot.container;
    }
}

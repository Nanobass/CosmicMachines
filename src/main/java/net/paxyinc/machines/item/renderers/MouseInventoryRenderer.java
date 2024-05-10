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
import net.paxyinc.machines.ui.BaseItemElement;
import net.paxyinc.machines.ui.InGameUI;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import static dev.crmodders.flux.ui.UIRenderer.uiRenderer;

public class MouseInventoryRenderer extends InGameUI implements IItemInventoryRenderer {

    private BaseItemElement slot;

    public MouseInventoryRenderer(ItemInventory inventory) {
        slot = new BaseItemElement(inventory.slots.get(0));
        slot.width = 32;
        slot.height = 32;
        uiElements.add(slot);
    }

    @Override
    public InGameUI getUI() {
        return this;
    }

    @Override
    public void render(Viewport uiViewport, Camera uiCamera, Vector2 mouse) {
        slot.x = mouse.x;
        slot.y = mouse.y;
        slot.repaint();
        super.render(uiViewport, uiCamera, mouse);
    }

    @Override
    public BaseItemElement atMouse(Vector2 mouse) {
        return slot;
    }
}

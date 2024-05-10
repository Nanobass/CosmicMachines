package net.paxyinc.machines.item.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.crmodders.flux.ui.Component;
import dev.crmodders.flux.ui.UIRenderer;
import dev.crmodders.flux.ui.text.TextBatch;
import net.paxyinc.machines.item.*;
import net.paxyinc.machines.item.inventories.MouseInventory;
import net.paxyinc.machines.ui.BaseItemElement;
import net.paxyinc.machines.ui.InGameUI;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import javax.swing.text.View;

import java.util.List;

import static dev.crmodders.flux.ui.UIRenderer.uiRenderer;

public class MouseInventoryRenderer implements InGameUI, IItemInventoryRenderer {

    private BaseItemElement slot;

    public MouseInventoryRenderer(ItemInventory inventory) {
        slot = new BaseItemElement(inventory.slots.get(0));
        slot.shouldRenderBackground = false;
        slot.shouldRenderName = false;
        slot.width = 32;
        slot.height = 32;
    }

    @Override
    public InGameUI getUI() {
        return this;
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
}

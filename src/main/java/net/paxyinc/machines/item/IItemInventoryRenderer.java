package net.paxyinc.machines.item;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import space.earlygrey.shapedrawer.ShapeDrawer;

public interface IItemInventoryRenderer {

    void updateUI(Viewport uiViewport, Vector2 mouse);
    void render(Viewport uiViewport, Camera uiCamera);
    ItemSlotPosition atMouse(Vector2 mouse);

}

package net.paxyinc.machines.item;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import net.paxyinc.machines.ui.BaseItemElement;
import net.paxyinc.machines.ui.InGameUI;
import space.earlygrey.shapedrawer.ShapeDrawer;

public interface IItemInventoryRenderer extends InGameUI {

    BaseItemElement atMouse(Viewport viewport, Vector2 mouse);

    ItemInventory getInventory();

}
